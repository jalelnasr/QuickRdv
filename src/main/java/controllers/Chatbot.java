package controllers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Chatbot {



        @FXML
        private TextArea chatArea;

        @FXML
        private TextField messageField;

        @FXML
        private Button sendButton;

        private static final String API_KEY = "AIzaSyCJIJ2Cj51vD5UQ8xxpTsHK2QuQ3IwsI6M"; // Remplacez par votre clé API Gemini
        private static String MODEL_NAME = "models/gemini-1.5-flash"; // Modèle par défaut

        // Initialisation du contrôleur
        public void initialize() {
            // Vérifier les modèles disponibles
            String errorMessage = listAvailableModels();
            if (errorMessage != null) {
                chatArea.appendText("Erreur lors de l'initialisation du chatbot : " + errorMessage + "\n");
                sendButton.setDisable(true); // Désactiver le bouton si l'initialisation échoue
            }
        }

        // Méthode appelée lorsque l'utilisateur clique sur "Envoyer"
        @FXML
        private void sendMessage() {
            String userMessage = messageField.getText().trim();
            if (userMessage.isEmpty()) {
                return; // Ne rien faire si le message est vide
            }

            // Afficher le message de l'utilisateur dans la zone de chat
            chatArea.appendText("Utilisateur : " + userMessage + "\n");
            messageField.clear();

            // Désactiver le bouton pendant le traitement
            sendButton.setDisable(true);

            // Traiter la requête dans un thread séparé pour ne pas bloquer l'UI
            new Thread(() -> {
                String response = getChatbotResponse(userMessage);
                Platform.runLater(() -> {
                    chatArea.appendText("Chatbot : " + response + "\n\n");
                    sendButton.setDisable(false); // Réactiver le bouton après avoir reçu la réponse
                });
            }).start();
        }

        // Lister les modèles disponibles
        private String listAvailableModels() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1/models?key=" + API_KEY)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray models = jsonResponse.getJSONArray("models");

                    for (int i = 0; i < models.length(); i++) {
                        JSONObject model = models.getJSONObject(i);
                        if (model.has("name") && model.getString("name").equals("models/gemini-1.0-pro-vision-latest")) {
                            System.out.println("Skipping deprecated model: models/gemini-1.0-pro-vision-latest");
                            continue;
                        }
                        JSONArray supportedMethods = model.getJSONArray("supportedGenerationMethods");
                        if (supportedMethods != null) {
                            for (int j = 0; j < supportedMethods.length(); j++) {
                                if (supportedMethods.getString(j).equals("generateContent") &&
                                        model.has("name") &&
                                        model.getString("name").equals("models/gemini-1.5-flash")) {
                                    MODEL_NAME = model.getString("name");
                                    System.out.println("Found a suitable model: " + MODEL_NAME);
                                    return null;
                                }
                            }
                        }
                    }
                    return "No suitable model found with generateContent support that is not deprecated.";
                } else {
                    return "Error listing models: " + response.code() + " - " + response.message();
                }
            } catch (IOException e) {
                return "Error listing models: " + e.getMessage();
            }
        }

        // Obtenir la réponse du chatbot
        private String getChatbotResponse(String userMessage) {
            String errorMessage = listAvailableModels();
            if (errorMessage != null) {
                return "Error initializing chatbot: " + errorMessage;
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            JSONObject jsonRequest = new JSONObject();
            JSONArray contentsArray = new JSONArray();
            JSONObject partsObject = new JSONObject();
            partsObject.put("text", userMessage);
            JSONArray partsArray = new JSONArray();
            partsArray.put(partsObject);

            JSONObject contentObject = new JSONObject();
            contentObject.put("parts", partsArray);

            contentsArray.put(contentObject);
            jsonRequest.put("contents", contentsArray);

            RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));

            Request request = new Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1/" + MODEL_NAME + ":generateContent?key=" + API_KEY)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    return parseResponse(responseBody);
                } else {
                    System.out.println("API Error: " + response.code() + " - " + response.message());
                    System.out.println("Error Response: " + response.body().string());
                    return "Sorry, I couldn't understand your question. Error: " + response.code();
                }
            } catch (IOException e) {
                System.out.println("API Call Error: " + e.getMessage());
                return "An error occurred while communicating with the chatbot.";
            }
        }

        // Parser la réponse de l'API
        private String parseResponse(String responseBody) {
            try {
                JSONObject jsonResponse = new JSONObject(responseBody);
                if (jsonResponse.has("candidates")) {
                    return jsonResponse.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");
                }
                return "No response obtained from the chatbot.";
            } catch (Exception e) {
                System.err.println("Error parsing response: " + e.getMessage());
                return "Error parsing chatbot response.";
            }
        }
    }

