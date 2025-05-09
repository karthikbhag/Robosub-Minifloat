#include <WiFi.h>
#include <WebServer.h>
#include <ArduinoJson.h>
#include <Wire.h>
#include "MS5837.h"
#include <OneWire.h> 
#include <stdlib.h>

// Replace these with your actual network credentials
const char* ssid = "ESP32-Minifloat-Point";
const char* password = "RubberDuckies";

// Motor control pins
const int MOTOR_DIR = 21;
const int MOTOR_PWM = 19;
const int pwmChannel = 0;
const int pwmFreq = 5000;
const int pwmResolution = 8;

WebServer server(80);

void setup() {
  Serial.begin(115200);

  pinMode(MOTOR_DIR, OUTPUT);
  ledcSetup(pwmChannel, pwmFreq, pwmResolution);
  ledcAttachPin(MOTOR_PWM, pwmChannel);
  ledcWrite(pwmChannel, 0); // Start with motor off
  digitalWrite(MOTOR_DIR, HIGH); 
  ledcWrite(pwmChannel, 200);

  WiFi.begin(ssid, password);
  Serial.print("Connecting to WiFi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected!");
  Serial.println(WiFi.localIP());

  server.on("/post", HTTP_POST, handlePostRequest);
  server.begin();
  Serial.println("Server started.");
}

void loop() {
  server.handleClient();
}

void handlePostRequest() {
  if (!server.hasArg("plain")) {
    server.send(400, "text/plain", "Missing body");
    return;
  }

  String body = server.arg("plain");
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, body);

  if (error) {
    server.send(400, "text/plain", "Invalid JSON");
    return;
  }

  String action = doc["action"];
  Serial.print("Received action: ");
  Serial.println(action);

  if (action == "inflate") {
    digitalWrite(MOTOR_DIR, HIGH);
    ledcWrite(pwmChannel, 200);
    server.send(200, "text/plain", "Inflating...");
  } else if (action == "deflate") {
    digitalWrite(MOTOR_DIR, LOW);
    ledcWrite(pwmChannel, 200);
    server.send(200, "text/plain", "Deflating...");
  } else {
    ledcWrite(pwmChannel, 0);
    server.send(400, "text/plain", "Unknown action");
  }
}
