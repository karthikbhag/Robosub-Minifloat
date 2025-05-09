#include <WiFi.h>
#include <WebServer.h>
#include <ArduinoJson.h>
#include <Wire.h>
#include "MS5837.h"
#include <OneWire.h> 
#include <stdlib.h>

const int pwmPin = 19;
const int pwmChannel = 0;
const int freq = 5000;
const int resolution = 8;

// void setup() {
//   Serial.begin(115200);
//    ledcSetup(pwmChannel, freq, resolution);
//   ledcAttachPin(pwmPin, pwmChannel);
//   ledcWrite(pwmChannel, 128);  // 50% power
// }

// void loop() {
// }

// Replace these with your actual network credentials
const char* ssid = "ESP32-Minifloat-Point";
const char* password = "RubberDuckies";

// Motor control pins
const int MOTOR_DIR = 21;
const int MOTOR_PWM = 19;
// Motor control pins
const int MOTOR_DIR = 21;     // Direction control
const int MOTOR_PWM = 19;     // Strength control via PWM

WebServer server(80);

void setup() {
  Serial.begin(115200);

  pinMode(MOTOR_DIR, OUTPUT);
  pinMode(MOTOR_PWM, OUTPUT);

  analogWrite(MOTOR_PWM, 0); // Start motor off

  WiFi.begin(ssid, password);
  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected. IP: " + WiFi.localIP().toString());

  server.on("/post", HTTP_POST, handlePostRequest);
  server.begin();
  Serial.println("Server started");
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
  Serial.println("Received action: " + action);

  if (action == "inflate") {
    digitalWrite(MOTOR_DIR, HIGH);
    analogWrite(MOTOR_PWM, 200); // Strength = ~80%
    server.send(200, "text/plain", "Inflating...");
  } else if (action == "deflate") {
    digitalWrite(MOTOR_DIR, LOW);
    analogWrite(MOTOR_PWM, 200);
    server.send(200, "text/plain", "Deflating...");
  } else {
    analogWrite(MOTOR_PWM, 0);  // Stop motor
    server.send(400, "text/plain", "Unknown action");
  }
}
