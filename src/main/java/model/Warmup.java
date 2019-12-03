package model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Warmup
{

    private transient boolean warmup = false;
    private transient long delay = 0;
}
