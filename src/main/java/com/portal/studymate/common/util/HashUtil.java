package com.portal.studymate.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Utility class for hashing operations.
 */
public class HashUtil {

    /**
     * Computes the SHA-256 hash of the input string.
     *
     * @param input The string to hash
     * @return The SHA-256 hash of the input as a hexadecimal string
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}