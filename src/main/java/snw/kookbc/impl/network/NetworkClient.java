/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 KookBC contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package snw.kookbc.impl.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

// provide the basic HTTP/WebSocket call feature. Authenticated with Bot Token.
public class NetworkClient {
    private final String tokenWithPrefix;
    private final OkHttpClient client = new OkHttpClient();

    public NetworkClient(String token) {
        tokenWithPrefix = "Bot " + token;
    }

    public JsonObject get(String fullUrl) {
        return JsonParser.parseString(getRawContent(fullUrl)).getAsJsonObject().getAsJsonObject("data");
    }

    public JsonObject post(String fullUrl, Map<?, ?> body) {
        return JsonParser.parseString(postContent(fullUrl, body)).getAsJsonObject().getAsJsonObject("data");
    }

    public String getRawContent(String fullUrl) {
        Request request = new Request.Builder()
                .get()
                .url(fullUrl)
                .addHeader("Authorization", tokenWithPrefix)
                .build();
        return call(request);
    }

    public String postContent(String fullUrl, Map<?, ?> body) {
        return postContent(fullUrl, new Gson().toJson(body), "application/json");
    }

    public String postContent(String fullUrl, String body, String mediaType) {
        Request request = new Request.Builder()
                .post(
                        RequestBody.create(body, MediaType.parse(mediaType))
                )
                .url(fullUrl)
                .addHeader("Authorization", tokenWithPrefix)
                .build();
        return call(request);
    }

    public String call(Request request) {
        try (Response res = client.newCall(request).execute()) {
            if (res.body() != null) {
                String resStr = res.body().string();
                JsonObject object = JsonParser.parseString(resStr).getAsJsonObject();
                int status = object.get("code").getAsInt();
                if (status != 0) {
                    throw new RuntimeException(String.format("Unexpected Response Code: %s, message: %s", status, object.get("message").getAsString()));
                }
                return resStr;
            }
            return "";
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException when we attempting to call request.", e);
        }
    }

    @NotNull
    public WebSocket newWebSocket(@NotNull Request request, @NotNull WebSocketListener listener) {
        return client.newWebSocket(request, listener);
    }
}