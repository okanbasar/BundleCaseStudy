package app.bundle.casestudy.casetwo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RandomData {
    private final String timestamp;
    private final Integer random;
    private final String hash;

    public String toJSON() {
        String json = "{}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(this);
            System.out.println("ResultingJSONstring = " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public RandomData(String line) {
        String[] parts = line.split(",");
        this.timestamp = parts[0];
        this.random = Integer.parseInt(parts[1]);
        this.hash = parts[2];
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getRandom() {
        return random;
    }

    public String getHash() {
        return hash;
    }
}
