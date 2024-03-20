/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gamepikachu;

import com.mycompany.gamepikachu.controller.GameController;
import com.mycompany.gamepikachu.utils.Utils;
import javax.swing.SwingUtilities;

/**
 *
 * @author huyng
 */
public class GamePikachu {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController gameController = new GameController("Pikachu Game");
            gameController.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT);
            gameController.start();
        });
    }
}
