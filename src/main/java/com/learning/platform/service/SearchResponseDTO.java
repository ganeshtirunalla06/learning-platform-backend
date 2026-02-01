package com.learning.platform.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor 
class SearchResponseDTO { String query; List<SearchResultDTO> results; }
