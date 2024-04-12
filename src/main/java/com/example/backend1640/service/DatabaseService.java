package com.example.backend1640.service;

import java.io.IOException;

public interface DatabaseService {
    byte[] exportAllTablesToCSV() throws IOException;
}
