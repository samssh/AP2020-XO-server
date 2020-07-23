package ir.sam.XO.server.model;

import com.google.gson.annotations.Expose;
import ir.sam.XO.server.database.SaveAble;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
public class Player implements SaveAble {
    @Id
    @Getter
    @Setter
    @Expose
    private String username;
    @Column
    @Getter
    @Setter
    private String password;
    @Column
    @Getter
    @Setter
    @Expose
    private int score;
    @Column
    @Getter
    @Setter
    @Expose
    private int games;
    @Column
    @Getter
    @Setter
    @Expose
    private int lose;
    @Column
    @Getter
    @Setter
    @Expose
    private int win;
    @Column
    @Getter
    @Setter
    @Expose
    private int draw;
    @Enumerated(value = EnumType.STRING)
    @Getter
    @Setter
    @Expose
    private PlayerState state;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.games = 0;
        this.lose = 0;
        this.win = 0;
        this.draw = 0;
        this.state = PlayerState.HANGING_ON_MENU;
    }
}
