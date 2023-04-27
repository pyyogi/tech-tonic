package com.kursach.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String title;
    @NotBlank(message = "Пожалуйста заполните поле")
    private int price;
    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(min=20, message = "Слишком короткое описание")
    private String description;
    @NotBlank(message = "Пожалуйста заполните поле")
    private String type;
    @NotBlank(message = "Пожалуйста заполните поле")
    private String brand;
    @NotBlank(message = "Пожалуйста заполните поле")
    private String filename;
}
