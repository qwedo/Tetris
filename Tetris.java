package com.javarush.task.Projects.Tetris;

import java.awt.event.KeyEvent;

/**
 * Класс Tetris - содержит основной функционал игры.
 */
public class Tetris {

    public static Tetris game;
    private Field field;                //Поле с клетками
    private Figure figure;              //Фигурка

    private boolean isGameOver;         //Игра Окончена

    public Tetris(int width, int height) {
        field = new Field(width, height);
        figure = null;
    }

    public Field getField() {
        return field;
    }

    public Figure getFigure() {
        return figure;
    }

    /**
     * Основной цикл программы.
     * Тут происходят все важные действия
     */
    public void run() throws Exception {
        KeyboardObserver keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();

        isGameOver = false;
        //создаем первую фигурку посередине сверху: x - половина ширины, y - 0.
        figure = FigureFactory.createRandomFigure(field.getWidth() / 2, 0);

        while (!isGameOver) {
            if (keyboardObserver.hasKeyEvents()) {
                KeyEvent event = keyboardObserver.getEventFromTop();
                if (event.getKeyChar() == 'q') return;
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                    figure.left();
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                    figure.right();
                else if (event.getKeyCode() == 12)
                    figure.rotate();
                else if (event.getKeyCode() == KeyEvent.VK_SPACE)
                    figure.downMaximum();
            }

            step();             //делаем очередной шаг
            field.print();      //печатаем состояние "поля"
            Thread.sleep(300);  //пауза 300 миллисекунд - 1/3 секунды
        }

        //Выводим сообщение "Game Over"
        System.out.println("Game Over");
    }

    public void step() {
        figure.down();
        if (!figure.isCurrentPositionAvailable()) {
            figure.up();                    //поднимаем обратно
            figure.landed();                //приземляем
            isGameOver = figure.getY() <= 1;//если фигурка приземлилась на самом верху - игра окончена
            field.removeFullLines();        //удаляем заполненные линии
            figure = FigureFactory.createRandomFigure(field.getWidth() / 2, 0); //создаем новую фигурку
        }
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public static void main(String[] args) throws Exception {
        game = new Tetris(10, 20);
        game.run();
    }
}
