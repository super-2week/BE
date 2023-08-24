package com.supercoding.commerce03.repository.redis;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RedisHash("words")
public class Words {
    @Id
    private String searchWord;
    private List<String> words = new ArrayList<>();

}
