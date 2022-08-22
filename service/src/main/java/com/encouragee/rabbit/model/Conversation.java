package com.encouragee.rabbit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Getter
@Setter
public class Conversation implements Serializable {
    @JsonProperty
    private String name;
}
