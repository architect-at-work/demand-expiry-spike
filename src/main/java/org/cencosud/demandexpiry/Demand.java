package org.cencosud.demandexpiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@RedisHash
@ToString(exclude = {"id"})
public class Demand {
    @Id
    private String id;

    private Status status;

    public Demand updateStatus(Status status) {
        this.status = status;
        return this;
    }
}


