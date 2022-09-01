package com.encouragee.rabbit;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Data
public class SimpleMessage implements Serializable {
    private String name;
}
