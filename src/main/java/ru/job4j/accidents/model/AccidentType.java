package ru.job4j.accidents.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class AccidentType {

    private int id;

    private String name;
}
