package com.example.bullsAndCows.attempt;

import com.example.bullsAndCows.game.model.Game;
import com.example.bullsAndCows.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attempts")
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String result;
}
