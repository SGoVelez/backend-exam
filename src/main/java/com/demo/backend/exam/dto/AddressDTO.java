package com.demo.backend.exam.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AddressDTO {

    private Long id;

    private String street;

    private String number;

    private String city;

    private String state;

    private String country;

    private String zipCode;

}
