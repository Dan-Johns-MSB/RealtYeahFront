package com.example.prethesispractice.helpers;

public class InputFieldsValidation {
    public static boolean checkPassportNumber(String passportNumber) {
        if (passportNumber == null) {
            return false;
        }

        return passportNumber.matches("^(?:[а-яА-Яa-zA-Z]{2}\\d{6}|\\d{9})$");
    }

    public static boolean checkDate(String date) {
        if (date == null) {
            return false;
        }

        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    public static boolean checkTime(String time) {
        if (time == null) {
            return false;
        }

        return time.matches("^\\d{2}:\\d{2}:\\d{2}$");
    }

    public static boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
    }
}
