package com.mycompany.gamepikachu.controller;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MatrixHelper {

    private static final int BLOCKED = -1; // Giá trị cho các ô bị chặn
    private static final int EMPTY = 0; // Giá trị cho các ô trống

    public static List<Point> findShortestPath(int[][] matrix, Point start, Point end) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Kiểm tra xem điểm start và end có hợp lệ không
        if (!isValidPoint(start, rows, cols) || !isValidPoint(end, rows, cols)) {
            return null;
        }

        // Tạo ma trận visited để đánh dấu các ô đã được duyệt qua
        boolean[][] visited = new boolean[rows][cols];

        // Tạo ma trận prev để lưu lại đỉnh trước đỉnh hiện tại trên đường đi ngắn nhất
        Point[][] prev = new Point[rows][cols];

        // Khởi tạo hàng đợi (queue) để duyệt các ô trong ma trận
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x][start.y] = true;

        // Duyệt BFS trên ma trận
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int x = current.x;
            int y = current.y;

            // Kiểm tra xem đã tìm được điểm kết thúc chưa
            if (x == end.x && y == end.y) {
                break;
            }

            // Duyệt qua các ô kề của ô hiện tại (chiều ngang và dọc)
            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                // Kiểm tra xem ô mới có hợp lệ và chưa được duyệt qua
                if (isValidPoint(newX, newY, rows, cols) && !visited[newX][newY]) {
                    // Kiểm tra xem ô mới có thể đi qua không
                    if (matrix[newX][newY] == EMPTY || matrix[newX][newY] == matrix[end.x][end.y]) {
                        queue.add(new Point(newX, newY));
                        visited[newX][newY] = true;
                        prev[newX][newY] = current;
                    }
                }
            }
        }

        // Xây dựng đường đi từ điểm kết thúc về điểm bắt đầu
        List<Point> path = new ArrayList<>();
        Point current = end;
        while (current != null) {
            path.add(0, current);
            current = prev[current.x][current.y];
        }

        return path;
    }

    private static boolean isValidPoint(Point point, int rows, int cols) {
        int x = point.x;
        int y = point.y;
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private static boolean isValidPoint(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }
}
