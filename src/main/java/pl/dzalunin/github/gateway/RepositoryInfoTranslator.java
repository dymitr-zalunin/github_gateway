package pl.dzalunin.github.gateway;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RepositoryInfoTranslator implements JsonSerializer<RepositoryInfo>, JsonDeserializer<RepositoryInfo> {
    @Override
    public RepositoryInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String fullName = json.getAsJsonObject().get("full_name").getAsString();
        String description = json.getAsJsonObject().get("description").getAsString();
        String cloneUrl = json.getAsJsonObject().get("clone_url").getAsString();
        int stars = json.getAsJsonObject().get("stargazers_count").getAsInt();
        String createdAt = json.getAsJsonObject().get("created_at").getAsString();

        return new RepositoryInfo(fullName, description, cloneUrl, stars, createdAt);
    }

    @Override
    public JsonElement serialize(RepositoryInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fullName", src.getFullName());
        jsonObject.addProperty("description", src.getDescription());
        jsonObject.addProperty("cloneUrl", src.getCloneUrl());
        jsonObject.addProperty("stars", src.getStars());
        jsonObject.addProperty("createdAt", src.getCreatedAt());

        return jsonObject;
    }
}
