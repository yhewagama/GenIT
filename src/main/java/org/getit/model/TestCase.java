package org.getit.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data

public class TestCase {
    private String method;
    private String url;
    private String response_path;
    private String request_body;
}
