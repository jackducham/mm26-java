package mech.mania.starter_pack.domain.memory;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.json.JSONObject;

public class MemoryObject {
    private final String PASSWORD, HOST, PORT, TARGET_ENGINE, TEAM_NAME;
    private final String USER_DATA_KEY;

    public static final String DEFAULT_STRING = "";
    public static final double DEFAULT_DOUBLE = 0.0;
    public static final int DEFAULT_INT = 0;
    public static final boolean DEFAULT_BOOLEAN = false;

    private RedisCommands<String, String> redisCommands;
    private JSONObject userData;

    public MemoryObject() {
        this.TARGET_ENGINE = System.getenv("TARGET_ENGINE");
        this.TEAM_NAME = System.getenv("TEAM_NAME");
        this.HOST  = System.getenv("REDIS_HOST");
        this.PORT = System.getenv("REDIS_PORT");
        this.PASSWORD = System.getenv("REDIS_PASSWORD");

        this.USER_DATA_KEY = formatUserDataKey(TARGET_ENGINE, TEAM_NAME);

        this.redisCommands = getConnection();
        fetchData();
    }

    public MemoryObject(String targetEngine, String teamName, String host,
                        String port, String password) {
        this.TARGET_ENGINE = targetEngine;
        this.TEAM_NAME = teamName;
        this.HOST  = host;
        this.PORT = port;
        this.PASSWORD = password;

        this.USER_DATA_KEY = formatUserDataKey(TARGET_ENGINE, TEAM_NAME);

        this.redisCommands = getConnection();
        fetchData();
    }

    public SetValueResult setValue(String key, Object value) {
        if (!isConnected()) {
            redisCommands = getConnection();
        }

        if (!isConnected()) {
            return SetValueResult.REDIS_NOT_CONNECTED;
        }

        if (!isValidValue(value)) {
            return SetValueResult.INVALID_OBJECT_TYPE;
        }

        userData.put(key, value);

        saveData();

        return SetValueResult.OPERATION_SUCCESS;
    }

    public int getValueInt(String key) {
        try {
            return userData.getInt(key);
        } catch (Exception e) {
            return DEFAULT_INT;
        }
    }

    public double getValueDouble(String key) {
        try {
            return userData.getDouble(key);
        } catch (Exception e) {
            return DEFAULT_DOUBLE;
        }
    }

    public boolean getValueBoolean(String key) {
        try {
            return userData.getBoolean(key);
        } catch (Exception e) {
            return DEFAULT_BOOLEAN;
        }
    }

    public String getValueString(String key) {
        try {
            return userData.getString(key);
        } catch (Exception e) {
            return DEFAULT_STRING;
        }
    }

    public boolean removeKey(String key) {
        if (!userData.has(key)) {
            return false;
        }

        userData.remove(key);

        return true;
    }

    public boolean isConnected() {
        return redisCommands != null && redisCommands.isOpen();
    }

    private String formatUserDataKey(String targetEngine, String teamName) {
        teamName = teamName.toLowerCase().replaceAll(" ", "_");

        return teamName.concat("_").concat(targetEngine);
    }

    private boolean isValidValue(Object value) {
        if (value == null) {
            return false;
        }

        switch (value.getClass().getName()) {
            case "java.lang.String":
            case "java.lang.Integer":
            case "java.lang.Double":
            case "java.lang.Boolean":
                return true;
            default:
                return false;
        }
    }

    private boolean fetchData() {
        if (!isConnected()) {
            redisCommands = getConnection();
        }

        if (!isConnected()) {
            userData = new JSONObject();
            return false;
        }

        String data = redisCommands.get(this.USER_DATA_KEY);

        if (data == null) {
            userData = new JSONObject();
            return false;
        }

        userData = new JSONObject(data);

        return true;
    }

    public boolean saveAndClose() {
        if (!saveData()) {
            return false;
        }

        return closeConnection();
    }

    public boolean saveData() {
        if (!isConnected()) {
            return false;
        }

        redisCommands.set(this.USER_DATA_KEY, userData.toString());

        return true;
    }

    public boolean closeConnection() {
        if (!isConnected()) {
            return false;
        }

        redisCommands.getStatefulConnection().close();

        return true;
    }

    private RedisCommands<String, String> getConnection() {
        String connectionString = String.format("redis://%s@%s:%s", PASSWORD, HOST, PORT);

        try {
            RedisClient client = RedisClient.create(connectionString);
            StatefulRedisConnection<String, String> redisConnection = client.connect();
            RedisCommands<String, String> syncCommands = redisConnection.sync();
            return syncCommands;
        } catch (Exception e) {
            System.out.println("Error at connection: " + e.toString());
            return null;
        }
    }
}