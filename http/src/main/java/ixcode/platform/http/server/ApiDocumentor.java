package ixcode.platform.http.server;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ixcode.platform.json.printer.JsonPrettyPrinter.prettyPrintJson;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

public class ApiDocumentor {

    public final UUID lastUuid = null;
    public final UUID[] uuids = {};

    public void GET(String url, String json) {
        String mutatedUrl = url;
        if (url.contains("{uuid}")) {
            addUuid(randomUUID());
            mutatedUrl = url.replace("{uuid}", lastUuid.toString());
        }

        print_json_GET(mutatedUrl, json);
    }

    public static void print_json_GET(String mutatedUrl, String json) {
        out.println("GET " + mutatedUrl + " HTTP/1.1");
        out.println("HTTP/1.1 200 Ok");
        out.println(prettyPrintJson(json));
        out.println();
    }

    private void addUuid(UUID uuid) {
        try {
            Field lastUuidField = ApiDocumentor.class.getField("lastUuid");
            Field uuidsField = ApiDocumentor.class.getField("uuids");

            lastUuidField.setAccessible(true);
            uuidsField.setAccessible(true);

            lastUuidField.set(this, uuid);

            List<UUID> uuidList = new ArrayList<UUID>(asList(uuids));
            uuidList.add(uuid);
            UUID[] newArray = uuidList.toArray(new UUID[]{});

            uuidsField.set(this, newArray);


        } catch (Exception e) {
            throw new RuntimeException("Problems with jiggery-pokery (see cause)", e);
        }

    }
}