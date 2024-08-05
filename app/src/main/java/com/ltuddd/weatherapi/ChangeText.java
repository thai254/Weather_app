package com.ltuddd.weatherapi;

public class ChangeText {
    private String output;

    public ChangeText(String input) {
        output = input;

        output = output.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        output = output.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        output = output.replaceAll("[ìíịỉĩ]", "i");
        output = output.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        output = output.replaceAll("[ùúụủũưừứựửữ]", "u");
        output = output.replaceAll("[ỳýỵỷỹ]", "y");
        output = output.replaceAll("[đ]", "d");
        output = output.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        output = output.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        output = output.replaceAll("[ÌÍỊỈĨ]", "I");
        output = output.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        output = output.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        output = output.replaceAll("[ỲÝỴỶỸ]", "Y");
    }

    public String getOutput() {
        return output;
    }
}