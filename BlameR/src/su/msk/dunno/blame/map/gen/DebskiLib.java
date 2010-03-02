/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package su.msk.dunno.blame.map.gen;

import bigtroubles.Creatures.Creature;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

import bigtroubles.Utils.StopWatch;
import bigtroubles.Utils.RNG;

/**
 *
 * @author Sanja
 * Набор генераторов уровней
 * 
 * Адаптация алгоритмов из C++ библиотеки "RoguelikeLib 0.4 (с) Jakub Debski"
 */
public class DebskiLib {
    static int LevelElementWall  = '#';
    static int LevelElementCorridor  = '.';
    static int LevelElementGrass  = '"';
    static int LevelElementPlant  = '&';
    static int LevelElementRoom  = ',';
    static int LevelElementDoorClose  = '+';
    static int LevelElementDoorOpen  = '/';
    static int LevelElementWater  = '~';
    public static int LevelElementCorridor_value  = Integer.MAX_VALUE - 2; // Some algorithms (like pathfinding) needs values instead of tiles
    public static int LevelElementRoom_value  = Integer.MAX_VALUE - 1;
    public static int LevelElementWall_value  = Integer.MAX_VALUE;
    
    static StopWatch watch;
    
    //--------------------------------------------------------------------------
    
    /*::: Генераторы :::*/
    
    // default CreateAntNest
    public static void CreateAntNest(Map level) 
    {
        CreateAntNest(level, false);
    }
    public static int[][] CreateAntNest(int N_x, int N_y, boolean with_rooms) 
    {
        int[][] level = new int[N_x][N_y];

        int x, y;

        level[N_x/2][N_y/2] = LevelElementCorridor;

        double x1, y1;
        double k;
        double dx, dy;
        int px, py;

        for (int object = 0; object < N_x*N_y/3; ++object) 
        {
            // degree
            k = Random(360) * 3.1419532 / 180;
            // position on ellipse by degree
            x1 = N_x/2 + N_x*sin(k);
            y1 = N_y/2 + (N_y/2)*cos(k);

            // object will move not too horizontal and not too vertival
            do {
                dx = Random(100);
                dy = Random(100);
            } while ((abs((int) dx) < 10 && abs((int) dy) < 10));
            dx -= 50;
            dy -= 50;
            dx /= 100;
            dy /= 100;

            int counter = 0;
            while (true) {
                // didn't catch anything after 1000 steps (just to avoid infinite loops)
                if (counter++ > 1000) {
                    object--;
                    break;
                }
                // move object by small step
                x1 += dx;
                y1 += dy;

                // change float to int

                px = (int) x1;
                py = (int) y1;

                // go through the border to the other side

                if (px < 0) {
                    px = N_x - 1;
                    x1 = px;
                }
                if (px > N_x - 1) {
                    px = 0;
                    x1 = px;
                }
                if (py < 0) {
                    py = N_y - 1;
                    y1 = py;
                }
                if (py > N_y - 1) {
                    py = 0;
                    y1 = py;
                }

                // if object has something to catch, then catch it

                if ((px > 0 && level[px - 1][py] == LevelElementCorridor) ||
                        (py > 0 && level[px][py - 1] == LevelElementCorridor) ||
                        (px < N_x-1 && level[px + 1][py] == LevelElementCorridor) ||
                        (py < N_y-1 && level[px][py+1] == LevelElementCorridor)) {
                    level[px][py] = LevelElementCorridor;
                    break;
                }
            }

        }

        if (with_rooms) {
            // add halls at the end of corridors
            for (y = 1; y < (int) level.GetHeight() - 1; y++) {
                for (x = 1; x < (int) level.GetWidth() - 1; x++) {
                    if ((x > (int) level.GetWidth() / 2 - 10 && x < (int) level.GetWidth() / 2 + 10 && y > (int) level.GetHeight() / 2 - 5 && y < (int) level.GetHeight() / 2 + 5) || level.GetCell(x, y) == LevelElementWall) {
                        continue;
                    }

                    int neighbours = CountNeighboursOfType(level, LevelElementCorridor, new Point(x, y));

                    if (neighbours == 1) {
                        for (px = -1; px <= 1; px++) {
                            for (py = -1; py <= 1; py++) {
                                level.SetCell(x + px, y + py, LevelElementRoom);
                            }
                        }
                    }
                }
            }
        } // end of if (with_rooms)
    }

    // default CreateCaves
    public static void CreateCaves(Map level) {
        CreateCaves(level, 1, 0.65f);
    }
    public static void CreateCaves(Map level, int iterations, float density) {
        if (level.GetWidth() == 0 || level.GetHeight() == 0) {
            return;
        }

        level.Clear(LevelElementRoom);

        // create a game of life cave

        int x, y;

        for (int fill = 0; fill < (level.GetWidth() * level.GetHeight() * density); fill++) {
            level.SetCell(Random((int) level.GetWidth()), Random((int) level.GetHeight()), LevelElementWall);
        }

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (x = 0; x < (int) level.GetWidth(); x++) {
                for (y = 0; y < (int) level.GetHeight(); y++) {
                    int neighbours = CountNeighboursOfType(level, LevelElementWall, new Point(x, y));

                    if (level.GetCell(x, y) == LevelElementWall) {
                        if (neighbours < 4) {
                            level.SetCell(x, y, LevelElementRoom);
                        }
                    } else {
                        if (neighbours > 4) {
                            level.SetCell(x, y, LevelElementWall);
                        }
                    }

                    if (x == 0 || x == (int) level.GetWidth() - 1 || y == 0 || y == (int) level.GetHeight() - 1) {
                        level.SetCell(x, y, LevelElementWall);
                    }
                }
            }
        }
        
        ConnectClosestRooms(level, true);        
        ConvertValuesToTiles(level);
    }

    // default CreateMaze
    public static void CreateMaze(Map level) {
        CreateMaze(level, false);
    }
    public static void CreateMaze(Map level, boolean allow_loops) {
        if (level.GetWidth() == 0 || level.GetHeight() == 0) {
            return;
        }

        level.Clear();

        LinkedList<Point> drillers = new LinkedList<Point>();
        drillers.addLast(new Point(level.GetWidth() / 2, level.GetHeight() / 2));
        while (drillers.size() > 0) {
            for (int i = 0; i < drillers.size(); i++) {
                boolean remove_driller = false;
                Point m = drillers.get(i);

                switch (Random(4)) {
                    case 0:
                        m.y -= 2;
                        if (m.y < 0 || (level.GetCell(m.x, m.y) == LevelElementCorridor)) {
                            boolean b;
                            if (Random(5) == 0) {
                                b = false;
                            } else {
                                b = true;
                            }

                            if (!allow_loops || (allow_loops && b)) {
                                remove_driller = true;
                                break;
                            }
                        }
                        level.SetCell(m.x, m.y + 1, LevelElementCorridor);
                        break;
                    case 1:
                        m.y += 2;
                        if (m.y >= level.GetHeight() || level.GetCell(m.x, m.y) == LevelElementCorridor) {
                            remove_driller = true;
                            break;
                        }
                        level.SetCell(m.x, m.y - 1, LevelElementCorridor);
                        break;
                    case 2:
                        m.x -= 2;
                        if (m.x < 0 || level.GetCell(m.x, m.y) == LevelElementCorridor) {
                            remove_driller = true;
                            break;
                        }
                        level.SetCell(m.x + 1, m.y, LevelElementCorridor);
                        break;
                    case 3:
                        m.x += 2;
                        if (m.x >= level.GetWidth() || level.GetCell(m.x, m.y) == LevelElementCorridor) {
                            remove_driller = true;
                            break;
                        }
                        level.SetCell(m.x - 1, m.y, LevelElementCorridor);
                        break;
                    }
                if (remove_driller) {
                    drillers.remove(m);
                } else {
                    drillers.addLast(new Point(m.x, m.y));
                    drillers.addLast(new Point(m.x, m.y));

                    level.SetCell(m.x, m.y, LevelElementCorridor);
                    ++i;
                }
            }
        }
    }

    // default CreateMines
    public static void CreateMines(Map level) {
        CreateMines(level, 10);
    }
    public static void CreateMines(Map level, int max_number_of_rooms) {
        if (level.GetWidth() == 0 || level.GetHeight() == 0) {
            return;
        }

        level.Clear();

        int x, y, sx, sy;

        LinkedList<SRoom> rooms = new LinkedList<SRoom>();

        SRoom room = new SRoom();
        SRoom m = new SRoom();

        int random_number;
        int diff_x, diff_y;

        Point p = new Point();
        Point p1 = new Point();
        Point p2 = new Point();

        // Place rooms

        for (int room_number = 0; room_number < max_number_of_rooms; ++room_number) {
            // size of room
            sx = Random(5) + 6;
            sy = Random(5) + 6;
            if (FindOnMapRandomRectangleOfType(level, LevelElementWall, p, new Point(sx + 4, sy + 4))) {
                p.x += 2;
                p.y += 2;
                // Connect the room to existing one

                if (rooms.size() > 0) {
                    
                    m = rooms.get(Random(rooms.size()));

                    // center of this room
                    p1.x = p.x + sx / 2;
                    p1.y = p.y + sy / 2;
                    // center of second room
                    p2.x = m.corner1.x + (m.corner2.x - m.corner1.x) / 2;
                    p2.y = m.corner1.y + (m.corner2.y - m.corner1.y) / 2;
                    // found the way to connect rooms

                    diff_x = p2.x - p1.x;
                    diff_y = p2.y - p1.y;

                    if (diff_x < 0) {
                        diff_x = -diff_x;
                    }
                    if (diff_y < 0) {
                        diff_y = -diff_y;
                    }

                    x = p1.x;
                    y = p1.y;

                    while (!(diff_x == 0 && diff_y == 0)) {
                        if (RandomLowerThatLimit(diff_x, diff_x + diff_y)) // move horizontally
                        {
                            diff_x--;
                            if (x > p2.x) {
                                x--;
                            } else {
                                x++;
                            }
                        } else {
                            diff_y--;
                            if (y > p2.y) {
                                y--;
                            } else {
                                y++;
                            }
                        }
                        // Check what is on that position
                        if (level.GetCell(x, y) == LevelElementRoom) {
                            break;
                        } else if (level.GetCell(x, y) == LevelElementCorridor) {
                            if (CoinToss()) {
                                break;
                            }
                        }

                        level.SetCell(x, y, LevelElementCorridor);
                    }
                }
                // add to list of rooms

                room.corner1.x = p.x;
                room.corner1.y = p.y;
                room.corner2.x = p.x + sx;
                room.corner2.y = p.y + sy;
                room.type = room_number;
                rooms.addLast(room);

                // draw_room

                int room_type = Random(4);
                if (sx == sy) {
                    room_type = 3;
                }

                if (room_type != 2) {
                    for (y = 0; y < sy; y++) {
                        for (x = 0; x < sx; x++) {
                            switch (room_type) {
                                case 0: // rectangle room
                                case 1:
                                    level.SetCell(p.x + x, p.y + y, LevelElementRoom);
                                    break;
                                case 3: // round room
                                    if (Distance(sx / 2, sx / 2, x, y) < sx / 2) {
                                        level.SetCell(p.x + x, p.y + y, LevelElementRoom);
                                    }
                                    break;
                            }
                        }
                    }
                } // end if
                else // typ==2 - Diamond
                {
                    for (y = 0; y <= sy / 2; y++) {
                        for (x = 0; x <= sx / 2; x++) {
                            if (y >= x) {
                                level.SetCell(p.x + x + sx / 2, p.y + y, LevelElementRoom);
                                level.SetCell(p.x + x + sx / 2, p.y + sy - y, LevelElementRoom);
                                level.SetCell(p.x + sx / 2 - x, p.y + y, LevelElementRoom);
                                level.SetCell(p.x + sx / 2 - x, p.y + sy - y, LevelElementRoom);
                            }
                        }
                    }
                }
            } // end of room addition
        }
    }

    // default CreateStandartDungeon
    public static void CreateStandardDunegon(Map level) {
        CreateStandardDunegon(level, 10, true);
    }
    public static void CreateStandardDunegon(Map level, int max_number_of_rooms, boolean with_doors) {
        if (level.GetWidth() == 0 || level.GetHeight() == 0) {
            return;
        }

        level.Clear();

        Point p = new Point();
        Point room_size = new Point();

        // place rooms
        for (int room_number = 0; room_number < max_number_of_rooms; ++room_number) {
            // size of room
            room_size.x = Random(5) + 8;
            room_size.y = Random(5) + 5;
            if (FindOnMapRandomRectangleOfType(level, LevelElementWall, p, room_size)) {
                for (int x = 1; x < room_size.x - 1; x++) {
                    for (int y = 1; y < room_size.y - 1; y++) {
                        level.SetCell(p.x + x, p.y + y, LevelElementRoom);
                    }
                }
            }
        }

        ConnectClosestRooms(level, true, true); // changes tiles to values
        ConvertValuesToTiles(level);
        if (with_doors) {
            AddDoors(level, 1, 0.5f);
        }
    }


    //--------------------------------------------------------------------------

    private static void FindOnMapAllRectanglesOfType(Map level, int type, Point size, LinkedList<Point> positions ) {
        Map good_points;
        good_points = new Map(null, "GoodPoints", level.width, level.height);

        for (int y = 0; y < level.GetHeight(); ++y) {
            for (int x = 0; x < level.GetWidth(); ++x) {
                good_points.SetCell(x, y, 0);
            }
        }
        
        // count horizontals

        for (int y = 0; y < level.GetHeight(); ++y) {
            int horizontal_count = 0;
            for (int x = 0; x < level.GetWidth(); ++x) {
                if (level.GetCell(x, y) == type) {
                    horizontal_count++;
                } else {
                    horizontal_count = 0;
                }

                if (horizontal_count == size.x) {
                    good_points.SetCell(x - size.x + 1, y, 1);
                    horizontal_count--;
                }
            }
        }

        // count verticals

        for (int x = 0; x < level.GetWidth(); ++x) {
            int vertical_count = 0;
            for (int y = 0; y < level.GetHeight(); ++y) {
                if (good_points.GetCell(x, y) == 1) {
                    vertical_count++;
                } else {
                    vertical_count = 0;
                }

                if (vertical_count == size.y) {
                    positions.addLast(new Point(x, y - size.y + 1));
                    vertical_count--;
                }
            }
        }
    }

    // default FloodFill
    private static boolean FloodFill(Map level, Point position, int value) {
        return FloodFill(level, position, value, true, 0, new Point(-1, -1));
    }
    private static boolean FloodFill(Map level, Point position, int value, boolean diagonal, int gradient, Point end) {
        // flood fill room
        int area_value = level.GetCell(position.x, position.y);
        level.SetCell(position.x, position.y, value);

        LinkedList<Point> positions = new LinkedList<Point>();
        Point m = new Point();
        positions.addLast(position);

        
        for (int i = 0; i < positions.size();) {
            m = positions.get(i);

            // Fill only to the end?
            if (end.x != -1 && end.equals(m)) {
                break;
            }

            int pos_x = m.x;
            int pos_y = m.y;

            int this_value = level.GetCell(pos_x, pos_y);

            if (pos_x > 0) {
                if (level.GetCell(pos_x - 1, pos_y) == area_value) {
                    level.SetCell(pos_x - 1, pos_y, this_value + gradient);
                    positions.addLast(new Point(pos_x - 1, pos_y));
                }
            }

            if (pos_x < (int) level.GetWidth() - 1) {
                if (level.GetCell(pos_x + 1, pos_y) == area_value) {
                    level.SetCell(pos_x + 1, pos_y, this_value + gradient);
                    positions.addLast(new Point(pos_x + 1, pos_y));
                }
            }

            if (pos_y > 0) {
                if (level.GetCell(pos_x, pos_y - 1) == area_value) {
                    level.SetCell(pos_x, pos_y - 1, this_value + gradient);
                    positions.addLast(new Point(pos_x, pos_y - 1));
                }
            }

            if (pos_y < (int) level.GetHeight() - 1) {
                if (level.GetCell(pos_x, pos_y + 1) == area_value) {
                    level.SetCell(pos_x, pos_y + 1, this_value + gradient);
                    positions.addLast(new Point(pos_x, pos_y + 1));
                }
            }

            if (diagonal) {
                if (pos_x > 0 && pos_y > 0) {
                    if (level.GetCell(pos_x - 1, pos_y - 1) == area_value) {
                        level.SetCell(pos_x - 1, pos_y - 1, this_value + gradient);
                        positions.addLast(new Point(pos_x - 1, pos_y - 1));
                    }
                }

                if (pos_x < (int) level.GetWidth() - 1 && pos_y < (int) level.GetHeight() - 1) {
                    if (level.GetCell(pos_x + 1, pos_y + 1) == area_value) {
                        level.SetCell(pos_x + 1, pos_y + 1, this_value + gradient);
                        positions.addLast(new Point(pos_x + 1, pos_y + 1));
                    }
                }

                if (pos_x < (int) level.GetWidth() - 1 && pos_y > 0) {
                    if (level.GetCell(pos_x + 1, pos_y - 1) == area_value) {
                        level.SetCell(pos_x + 1, pos_y - 1, this_value + gradient);
                        positions.addLast(new Point(pos_x + 1, pos_y - 1));
                    }
                }

                if (pos_x > 0 && pos_y < (int) level.GetHeight() - 1) {
                    if (level.GetCell(pos_x - 1, pos_y + 1) == area_value) {
                        level.SetCell(pos_x - 1, pos_y + 1, this_value + gradient);
                        positions.addLast(new Point(pos_x - 1, pos_y + 1));
                    }
                }
            }

            m = positions.remove(i);
        }

        return true;
    }

    private static boolean FindOnMapRandomRectangleOfType(Map level, int type, Point pos, Point size) {
        LinkedList<Point> positions = new LinkedList<Point>();
        FindOnMapAllRectanglesOfType(level, type, size, positions);
        if (positions.size() == 0) {
            return false;
        }

        // get position of Random rectangle 
        int rnd = Random(positions.size());
        pos.move(positions.get(rnd).x, positions.get(rnd).y);

        return true;
    }

    // default CountNeighboursOfType
    private static int CountNeighboursOfType(Map level, int type, Point pos) {
        return CountNeighboursOfType(level, type, pos, true);
    }
    private static int CountNeighboursOfType(Map level, int type, Point pos, boolean diagonal) {
        int neighbours = 0;
        if (pos.y > 0) {
            if (level.GetCell(pos.x, pos.y - 1) == type) // N
            {
                neighbours++;
            }
        }

        if (pos.x < (int) level.GetWidth() - 1) {
            if (level.GetCell(pos.x + 1, pos.y) == type) // E
            {
                neighbours++;
            }
        }

        if (pos.x > 0 && pos.y < (int) level.GetHeight() - 1) {
            if (level.GetCell(pos.x, pos.y + 1) == type) // S
            {
                neighbours++;
            }
        }

        if (pos.x > 0 && pos.y > 0) {
            if (level.GetCell(pos.x - 1, pos.y) == type) // W
            {
                neighbours++;
            }
        }

        if (diagonal) {
            if (pos.x > 0 && pos.y > 0) {
                if (level.GetCell(pos.x - 1, pos.y - 1) == type) // NW
                {
                    neighbours++;
                }
            }

            if (pos.x < (int) level.GetWidth() - 1 && pos.y > 0) {
                if (level.GetCell(pos.x + 1, pos.y - 1) == type) // NE
                {
                    neighbours++;
                }
            }

            if (pos.x < (int) level.GetWidth() - 1 && pos.y < (int) level.GetHeight() - 1) // SE
            {
                if (level.GetCell(pos.x + 1, pos.y + 1) == type) {
                    neighbours++;
                }
            }


            if (pos.x > 0 && pos.y < (int) level.GetHeight() - 1) {
                if (level.GetCell(pos.x - 1, pos.y + 1) == type) // SW
                {
                    neighbours++;
                }
            }
        }

        return neighbours;
    }

    private static void AddDoors(Map level, float door_probability, float open_probability) {
        for (int x = 0; x < level.GetWidth(); ++x) {
            for (int y = 0; y < level.GetHeight(); ++y) {
                Point pos = new Point(x, y);
                int room_cells = CountNeighboursOfType(level, LevelElementRoom, pos);
                int corridor_cells = CountNeighboursOfType(level, LevelElementCorridor, pos);
                int open_door_cells = CountNeighboursOfType(level, LevelElementDoorOpen, pos);
                int close_door_cells = CountNeighboursOfType(level, LevelElementDoorClose, pos);
                int door_cells = open_door_cells + close_door_cells;

                if (level.GetCell(x, y) == LevelElementCorridor) {
                    if ((corridor_cells == 1 && door_cells == 0 && room_cells > 0 && room_cells < 4) ||
                            (corridor_cells == 0 && door_cells == 0)) {
                        float exist = ((float) Random(1000)) / 1000;
                        if (exist < door_probability) {
                            float is_open = ((float) Random(1000)) / 1000;
                            if (is_open < open_probability) {
                                level.SetCell(x, y, LevelElementDoorOpen);
                            } else {
                                level.SetCell(x, y, LevelElementDoorClose);
                            }
                        }
                    }
                }
            }
        }
    }

    // default AddCoridor
    private static boolean AddCorridor(Map level, int start_x1, int start_y1, int start_x2, int start_y2) {
        return AddCorridor(level, start_x1, start_y1, start_x2, start_y2, false);
    }
    private static boolean AddCorridor(Map level, int start_x1, int start_y1, int start_x2, int start_y2, boolean straight) {	
        if (!level.OnMap(start_x1, start_y1) || !level.OnMap(start_x2, start_y2)) {
            return false;
        }
        // we start from both sides 
        int x1, y1, x2, y2;

        x1 = start_x1;
        y1 = start_y1;
        x2 = start_x2;
        y2 = start_y2;

        int dir_x;
        int dir_y;

        if (start_x2 > start_x1) {
            dir_x = 1;
        } else {
            dir_x = -1;
        }

        if (start_y2 > start_y1) {
            dir_y = 1;
        } else {
            dir_y = -1;
        }


        // move into direction of the other end
        boolean first_horizontal = CoinToss();
        boolean second_horizontal = CoinToss();

        while (true) {
            if (!straight) {
                first_horizontal = CoinToss();
                second_horizontal = CoinToss();
            }

            if (x1 != x2 && y1 != y2) {
                if (first_horizontal) {
                    x1 += dir_x;
                } else {
                    y1 += dir_y;
                }
            }
            // connect rooms
            if (x1 != x2 && y1 != y2) {
                if (second_horizontal) {
                    x2 -= dir_x;
                } else {
                    y2 -= dir_y;
                }
            }

            if (level.GetCell(x1, y1) == LevelElementWall_value) {
                level.SetCell(x1, y1, LevelElementCorridor_value);
            }
            if (level.GetCell(x2, y2) == LevelElementWall_value) {
                level.SetCell(x2, y2, LevelElementCorridor_value);
            }

            // connect corridors if on the same level
            if (x1 == x2) {
                while (y1 != y2) {
                    y1 += dir_y;
                    if (level.GetCell(x1, y1) == LevelElementWall_value) {
                        level.SetCell(x1, y1, LevelElementCorridor_value);
                    }
                }
                if (level.GetCell(x1, y1) == LevelElementWall_value) {
                    level.SetCell(x1, y1, LevelElementCorridor_value);
                }
                return true;
            }
            if (y1 == y2) {
                while (x1 != x2) {
                    x1 += dir_x;
                    if (level.GetCell(x1, y1) == LevelElementWall_value) {
                        level.SetCell(x1, y1, LevelElementCorridor_value);
                    }
                }
                if (level.GetCell(x1, y1) == LevelElementWall_value) {
                    level.SetCell(x1, y1, LevelElementCorridor_value);
                }
                return true;
            }
        }
        //return true;
    }

    private static int FillDisconnectedRoomsWithDifferentValues(Map level) {
        for (int y = 0; y < level.GetHeight(); ++y) {
            for (int x = 0; x < level.GetWidth(); ++x) {
                if (level.GetCell(x, y) == LevelElementRoom) {
                    level.SetCell(x, y, LevelElementRoom_value);
                } else if (level.GetCell(x, y) == LevelElementWall) {
                    level.SetCell(x, y, LevelElementWall_value);
                }
            }
        }

        int room_number = 0;

        for (int y = 0; y < level.GetHeight(); ++y) {
            for (int x = 0; x < level.GetWidth(); ++x) {
                if (level.GetCell(x, y) == LevelElementRoom_value) {
                    FloodFill(level, new Point(x, y), room_number++);
                }
            }
        }
        return room_number;
    }

    // default ConnectClosestRooms
    private static void ConnectClosestRooms(Map level, boolean with_doors) {
        ConnectClosestRooms(level, with_doors, false);
    }
    private static void ConnectClosestRooms(Map level, boolean with_doors, boolean straight_connections) {
        FillDisconnectedRoomsWithDifferentValues(level);

        LinkedList<LinkedList<Point>> rooms = new LinkedList<LinkedList<Point>>();
        
        for (int y = 0; y < level.GetHeight(); ++y) {
            for (int x = 0; x < level.GetWidth(); ++x) {
                if (level.GetCell(x, y) != LevelElementWall_value) {
                    if (level.GetCell(x, y) >= (int) rooms.size()) {
                        rooms.addLast(new LinkedList<Point>());
                    }

                    if (CountNeighboursOfType(level, LevelElementWall_value, new Point(x, y), false) > 0) // only border cells without diagonals
                    {
                        rooms.get(level.GetCell(x, y)).addLast(new Point(x, y));
                    }
                }
            }
        }
        
        level.rooms = rooms;

        Collections.shuffle(rooms);
            

        if (rooms.size() < 2) {
            return;
        }

        // for warshall algorithm
        // set the connection matrix


        LinkedList<LinkedList<Boolean>> room_connections = new LinkedList<LinkedList<Boolean>>();
        LinkedList<LinkedList<Boolean>> transitive_closure = new LinkedList<LinkedList<Boolean>>();
        LinkedList<LinkedList<Integer>> distance_matrix = new LinkedList<LinkedList<Integer>>();
        LinkedList<LinkedList<Pair>> closest_cells_matrix = new LinkedList<LinkedList<Pair>>();

        for (int i = 0; i < rooms.size(); i++)
            room_connections.add(new LinkedList<Boolean>());
        for (int i = 0; i < rooms.size(); i++)
            transitive_closure.add(new LinkedList<Boolean>());
        for (int i = 0; i < rooms.size(); i++)
            distance_matrix.add(new LinkedList<Integer>());
        for (int i = 0; i < rooms.size(); i++)
            closest_cells_matrix.add(new LinkedList<Pair>());
        

        for (int a = 0; a < rooms.size(); ++a) {
            for (int i = 0; i < rooms.size(); i++)
                room_connections.get(a).add(true);
            for (int i = 0; i < rooms.size(); i++)
                transitive_closure.get(a).add(true);
            for (int i = 0; i < rooms.size(); i++)
                distance_matrix.get(a).add(0);
            for (int i = 0; i < rooms.size(); i++)
                closest_cells_matrix.get(a).add(make_pair(new Point(-1, -1), new Point(-1, -1)));

            for (int b = 0; b < rooms.size(); ++b) {
                room_connections.get(a).set(b, false);
                distance_matrix.get(a).set(b, Integer.MAX_VALUE);
            }
        }

        // find the closest cells for each room - Random closest cell
        
        for (int room_a = 0; room_a < (int) rooms.size(); ++room_a) {
            for (int room_b = 0; room_b < (int) rooms.size(); ++room_b) {
                if (room_a == room_b) {
                    continue;
                }
                
                Pair closest_cells = make_pair(new Point(), new Point());
                
                for (int m = 0; m < rooms.get(room_a).size(); m++) {                    
                    // for each boder cell in room_a try each border cell of room_b
                    int x1 = rooms.get(room_a).get(m).x;
                    int y1 = rooms.get(room_a).get(m).y;
                    
                    for (int k = 0; k < rooms.get(room_b).size(); k++) {
                        int x2 = rooms.get(room_b).get(k).x;
                        int y2 = rooms.get(room_b).get(k).y;

                        int dist_ab = (int)Distance(x1, y1, x2, y2);

                        if (dist_ab < distance_matrix.get(room_a).get(room_b) || (dist_ab == distance_matrix.get(room_a).get(room_b) && CoinToss())) {
                            closest_cells = make_pair(new Point(x1, y1), new Point(x2, y2));
                            distance_matrix.get(room_a).set(room_b, dist_ab);
                        }
                    }
                }
                closest_cells_matrix.get(room_a).set(room_b, closest_cells);
            }
        }

        // Now connect the rooms to the closest ones

        for (int room_a = 0; room_a < (int) rooms.size(); ++room_a) {
            int min_distance = Integer.MAX_VALUE;
            int closest_room = room_a;
            for (int room_b = 0; room_b < (int) rooms.size(); ++room_b) {
                if (room_a == room_b) {
                    continue;
                }
                int distance = distance_matrix.get(room_a).get(room_b);
                if (distance < min_distance) {
                    min_distance = distance;
                    closest_room = room_b;
                }
            }

            // connect room_a to closest one
            Pair closest_cells = make_pair(new Point(), new Point());
            closest_cells = closest_cells_matrix.get(room_a).get(closest_room);

            int x1 = closest_cells.first.x;
            int y1 = closest_cells.first.y;
            int x2 = closest_cells.second.x;
            int y2 = closest_cells.second.y;

            if (room_connections.get(room_a).get(closest_room) == false && AddCorridor(level, x1, y1, x2, y2, straight_connections)) {
                room_connections.get(room_a).set(closest_room, true);
                room_connections.get(closest_room).set(room_a, true);
            }
        }

        // The closest rooms connected. Connect the rest until all areas are connected
        for (int to_connect_a = 0; to_connect_a != -1;) {
            int a, b, c;
            int to_connect_b;

            for (a = 0; a < rooms.size(); a++) {
                for (b = 0; b < rooms.size(); b++) {
                    transitive_closure.get(a).set(b, room_connections.get(a).get(b));
                }
            }

            for (a = 0; a < rooms.size(); a++) {
                for (b = 0; b < rooms.size(); b++) {
                    if (transitive_closure.get(a).get(b) == true && a != b) {
                        for (c = 0; c < rooms.size(); c++) {
                            if (transitive_closure.get(b).get(c) == true) {
                                transitive_closure.get(a).set(c, true);
                                transitive_closure.get(c).set(a, true);
                            }
                        }
                    }
                }
            }

            // Check if all rooms are connected
            to_connect_a = -1;
            for (a = 0; a < rooms.size() && to_connect_a == -1; ++a) {
                for (b = 0; b < rooms.size(); b++) {
                    if (a != b && transitive_closure.get(a).get(b) == false) {
                        to_connect_a = (int) a;
                        break;
                    }
                }
            }

            if (to_connect_a != -1) {
                // connect rooms a & b
                do {
                    to_connect_b = Random((int) rooms.size());
                } while (to_connect_b == to_connect_a);
                Pair closest_cells = make_pair(new Point(), new Point());
                closest_cells = closest_cells_matrix.get(to_connect_a).get(to_connect_b);

                int x1 = closest_cells.first.x;
                int y1 = closest_cells.first.y;
                int x2 = closest_cells.second.x;
                int y2 = closest_cells.second.y;

                AddCorridor(level, x1, y1, x2, y2, straight_connections);

                room_connections.get(to_connect_a).set(to_connect_b, true);
                room_connections.get(to_connect_b).set(to_connect_a, true);
            }
        }
    }
    
    // default AddRecursiveRooms
    private static void AddRecursiveRooms(Map level, int type, int min_size_x, int min_size_y, SRoom room) {
        AddRecursiveRooms(level, type, min_size_x, min_size_y, room, true);
    }
    private static void AddRecursiveRooms(Map level, int type, int min_size_x, int min_size_y, SRoom room, boolean with_doors) {
        int size_x = room.corner2.x - room.corner1.x;

        if (size_x % 2 != 0) {
            size_x -= CoinToss() ? 1 : 0;
        }

        int size_y = room.corner2.y - room.corner1.y;
        if (size_y % 2 != 0) {
            size_y -= CoinToss() ? 1 : 0;
        }

        boolean split_horizontal;

        if (size_y * 4 > size_x) {
            split_horizontal = true;
        } else if (size_x * 4 > size_y) {
            split_horizontal = false;
        } else {
            split_horizontal = CoinToss();
        }

        if (split_horizontal) // split horizontal
        {
            if (size_y / 2 < min_size_y) {
                return;
            }
            int split = size_y / 2 + Random(size_y / 2 - min_size_y);
            for (int x = room.corner1.x; x < room.corner2.x; x++) {
                level.SetCell(x, room.corner1.y + split, type);
            }

            if (with_doors) {
                level.SetCell(room.corner1.x + Random(size_x - 1) + 1, room.corner1.y + split, LevelElementDoorClose);
            }

            SRoom new_room = room;
            new_room.corner2.y = room.corner1.y + split;
            AddRecursiveRooms(level, type, min_size_x, min_size_y, new_room, with_doors);

            new_room = room;
            new_room.corner1.y = room.corner1.y + split;
            AddRecursiveRooms(level, type, min_size_x, min_size_y, new_room, with_doors);
        } else {
            if (size_x / 2 < min_size_x) {
                return;
            }
            int split = size_x / 2 + Random(size_x / 2 - min_size_x);
            for (int y = room.corner1.y; y < room.corner2.y; y++) {
                level.SetCell(room.corner1.x + split, y, type);
            }

            if (with_doors) {
                level.SetCell(room.corner1.x + split, room.corner1.y + Random(size_y - 1) + 1, LevelElementDoorClose);
            }

            SRoom new_room = room;
            new_room.corner2.x = room.corner1.x + split;
            AddRecursiveRooms(level, type, min_size_x, min_size_y, new_room, with_doors);

            new_room = room;
            new_room.corner1.x = room.corner1.x + split;
            AddRecursiveRooms(level, type, min_size_x, min_size_y, new_room, with_doors);
        }
    }
   
    private static void ConvertValuesToTiles(Map level) {
        for (int y = 0; y < level.GetHeight(); ++y) {
            for (int x = 0; x < level.GetWidth(); ++x) {
                if (level.GetCell(x, y) == LevelElementCorridor_value) {
                    level.SetCell(x, y, LevelElementCorridor);
                } else if (level.GetCell(x, y) == LevelElementWall_value) {
                    level.SetCell(x, y, LevelElementWall);
                } else {
                    level.SetCell(x, y, LevelElementRoom);
                }
            }
        }
    }

    private static void DrawRectangleOnMap(Map level, Point p1, Point p2, int value) {
        for (int y = p1.y; y < p2.y; ++y) {
            for (int x = p1.x; x < p2.x; ++x) {
                level.SetCell(x, y, value);
            }
        }
    }
    
    
    //--------------------------------------------------------------------------
    
    /*::: Поиск пути :::*/
    // default FindPath
    public static boolean FindPath(Map level, Point start, Point end, LinkedList<Point> path) {
        return FindPath(level, start, end, path, true);
    }
    public static boolean FindPath(Map level, Point start, Point end, LinkedList<Point> path, boolean diagonals) {        
        // Hack: Если начальная и текущая клетка совпадают, сохраняем в path
        // стартовую клетку и выходим
        if (start.equals(end)) {
            path.add(start);
            return true;
        }
        
        // fill from end to start
        if (!FloodFill(level, end, 0, false, 1, start)) {
            return false;
        }

        // walk from start to end
        Point pos = new Point(start);
        Point new_pos = new Point(start);

        while (true) {
            if (pos.equals(end)) {
                return true;
            }
            pos = new Point(new_pos);

            if (!pos.equals(start)) {
                path.addLast(pos);
            }

            int current_value = level.GetCell(pos.x, pos.y);

            if (diagonals) {
                if (pos.x > 0 && pos.y > 0) {
                    if (level.GetCell(pos.x - 1, pos.y - 1) < current_value) // NW
                    {
                        new_pos.x--;
                        new_pos.y--;
                        continue;
                    }
                }

                if (pos.x < (int) level.GetWidth() - 1 && pos.y > 0) {
                    if (level.GetCell(pos.x + 1, pos.y - 1) < current_value) // NE
                    {
                        new_pos.x++;
                        new_pos.y--;
                        continue;
                    }
                }

                if (pos.x < (int) level.GetWidth() - 1 && pos.y < (int) level.GetHeight() - 1) // SE
                {
                    if (level.GetCell(pos.x + 1, pos.y + 1) < current_value) {
                        new_pos.x++;
                        new_pos.y++;
                        continue;
                    }
                }

                if (pos.x > 0 && pos.y < (int) level.GetHeight() - 1) {
                    if (level.GetCell(pos.x - 1, pos.y + 1) < current_value) // SW
                    {
                        new_pos.x--;
                        new_pos.y++;
                        continue;
                    }
                }
            }

            if (pos.y > 0) {
                if (level.GetCell(pos.x, pos.y - 1) < current_value) // N
                {
                    new_pos.y--;
                    continue;
                }
            }

            if (pos.x < (int) level.GetWidth() - 1) {
                if (level.GetCell(pos.x + 1, pos.y) < current_value) // E
                {
                    new_pos.x++;
                    continue;
                }
            }

            if (pos.x >= 0 && pos.y < (int) level.GetHeight() - 1) {
                if (level.GetCell(pos.x, pos.y + 1) < current_value) // S
                {
                    new_pos.y++;
                    continue;
                }
            }

            if (pos.x > 0 && pos.y >= 0) {
                if (level.GetCell(pos.x - 1, pos.y) < current_value) // W
                {
                    new_pos.x--;
                    continue;
                }
            }
            if (pos.equals(new_pos) && !new_pos.equals(end)) {
                path.clear();
                return false;
            }
        }
    }
    
    public static boolean MyFindPath(Map level, Point start, Point end, LinkedList<Integer> path) {
        Point cur = new Point(start);
        Point adj = new Point(start);
        int[][] weightMap = new int[level.width][level.height];
        LinkedList<Point> stack = new LinkedList<Point>();
        
        for (int i = 0; i < level.width; i++) {
            for (int j = 0; j < level.height; j++) {
                weightMap[i][j] = 0;
            }
        }
        
        while (true) {
            // 2
            for (int i = -1; i <= 1; i++) {                
                for (int j = -1; j <= 1; j++) {
                    if (level.OnMap(cur.x+i, cur.y+j) && weightMap[cur.x+i][cur.y+j] == 0) {
                        // Если ячейка текущая - продолжаем
                        if (i == 0 && j == 0)
                            continue;
                        
                        adj = new Point(cur.x+i, cur.y+j);
                     
                        // 3
                        weightMap[adj.x][adj.y] = weightMap[cur.x][cur.y] + 1;
                        
                        // 3 if
                        if (adj.equals(end)) {
                            // 7
                            cur = new Point(end);
                            
                            // 8
                            for (int k = -1; k <= 1; k++) {                
                                for (int l = -1; l <= 1; l++) {
                                    // 9 if
                                    if (level.OnMap(cur.x+k, cur.y+l) && weightMap[cur.x+k][cur.y+l] == weightMap[cur.x][cur.y]-1) {
                                        //Hack
                                        String s = "";
                                        for (int m = 0; m < level.width; m++) {
                                            for (int n = 0; n < level.height; n++) {
                                                s += Integer.valueOf(weightMap[m][n]);
                                            }
                                            System.out.println(s);
                                            s = "";
                                        }
                                            
                                        
                                        adj = new Point(cur.x+k, cur.y+l);
                                        // 10
                                        path.addLast(Map.getDirection(adj, end));
                                        cur = new Point(adj);    
                                        
                                        // 12
                                        if (cur.equals(start))
                                            return true;
                                    }
                                }
                            }
                        } else { // 3 else
                            // 4
                            stack.addFirst(adj);
                        }
                    }
                }
            }
            
            // 5
            if (!stack.isEmpty())
                cur = stack.removeFirst();
            else
                return true;
        }
    }
    
    public static LinkedList<Point> newFindPath(Map level, Point start, Point end) {
        LinkedList<Point> result = new LinkedList<Point>();
        LinkedList<Point> unne = new LinkedList<Point>();
        boolean[][] visited = new boolean[level.width][level.height];
        Point cur = new Point();
        
        for (int i = 0; i < level.width; i++) {
            for (int j = 0; j < level.height; j++) {
                visited[i][j] = false;
            }
        }
        
        boolean done = false;
        cur = new Point(start);
        unne.addLast(cur);
        while (unne.size() > 0) {
            cur = unne.removeFirst();
            if (cur.equals(end)) {
                result = new LinkedList(unne);
                done = true;
                break;
            } else {
                visited[cur.x][cur.y] = true;
                addNode(level, unne, visited, cur.x+1, cur.y);
                addNode(level, unne, visited, cur.x-1, cur.y);
                addNode(level, unne, visited, cur.x, cur.y+1);
                addNode(level, unne, visited, cur.x, cur.y-1);
            }
        }
        
        
        
        return result;
    }
    
    private static void addNode(Map level, LinkedList<Point> list, boolean[][] visited, int x, int y) {
        if (level.isWalkable(x, y) && !visited[x][y])
            list.addLast(new Point(x, y));
    }
    
    //-------------------------------------------------------------------------
    private static int abs(int value) {
        return java.lang.Math.abs(value);
    }
    private static double sin(double value) {
        return java.lang.Math.sin(value);
    }
    private static double cos(double value) {
        return java.lang.Math.cos(value);
    }
    
    private static int Random(int value) {
        return RNG.getInt(value);
    }
    private static boolean RandomLowerThatLimit(int limit, int value) {
        if (value == 0) {
            return false;
        }
        if (Random(value) < limit) {
            return true;
        }
        return false;
    }
    private static boolean CoinToss() {
        return (Random(2) != 0);
    }
    
    public static double Distance(int x1, int y1, int x2, int y2) {
        int dX = x2 - x1;
        int dY = y2 - y1;
        return java.lang.Math.sqrt(dX * dX + dY * dY);
    }
    public static double Distance(Point p1, Point p2) {
        return Distance(p1.x, p1.y, p2.x, p2.y);
    }
    
    private static Pair make_pair(Point first, Point second) {
        Pair result = new Pair();
        result.first = first;
        result.second = second;
        
        return result;
    }

    static class Pair extends LinkedList {
        Point first;
        Point second;
        
        public Pair() {
            first = new Point();
            second = new Point();
        }
    }
    
    static class SRoom {
        Point corner1, corner2;
        int type;
        
        public SRoom() {
            corner1 = new Point();
            corner2 = new Point();
            type = 0;
        }
        
        public boolean IsInRoom(Point pos) {
            return (pos.x >= corner1.x && pos.x <= corner2.x && pos.y >= corner1.y && pos.y <= corner2.y);
        }

        public boolean IsInRoom(int x, int y) {
            return (x >= corner1.x && x <= corner2.x && y >= corner1.y && y <= corner2.y);
        }
    }
}
