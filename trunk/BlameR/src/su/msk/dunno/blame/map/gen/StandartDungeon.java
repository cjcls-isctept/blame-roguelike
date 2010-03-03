package su.msk.dunno.blame.map.gen;

import java.util.Collections;
import java.util.LinkedList;

import su.msk.dunno.blame.map.gen.DebskiLib.Pair;
import su.msk.dunno.blame.support.Point;
import su.msk.dunno.blame.support.RNG;

public class StandartDungeon 
{
	static int LevelElementWall  = 0;
	static int LevelElementCorridor  = 1;
	static int LevelElementRoom  = 1;
    static int LevelElementDoorClose  = 2;
    static int LevelElementDoorOpen  = 1;
	public static int LevelElementCorridor_value  = Integer.MAX_VALUE - 2; 
	public static int LevelElementRoom_value  = Integer.MAX_VALUE - 1;
	public static int LevelElementWall_value  = Integer.MAX_VALUE;
	
    // default CreateStandartDungeon
    public static int[][] CreateStandardDunegon(int N_x, int N_y) 
    {
        return CreateStandardDunegon(N_x, N_y, 10, true);
    }
	
	public static int[][] CreateStandardDunegon(int N_x, int N_y, int max_number_of_rooms, boolean with_doors) 
	{
        int[][] level = new int[N_x][N_y];

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
                        level[p.x+x][p.y+y] = LevelElementRoom;
                    }
                }
            }
        }

        ConnectClosestRooms(level, true, true); // changes tiles to values
        ConvertValuesToTiles(level);
        if (with_doors) {
            AddDoors(level, 1, 0.5f);
        }
        
        return level;
    }
	
    private static boolean FindOnMapRandomRectangleOfType(int[][] level, int type, Point pos, Point size) 
    {
        LinkedList<Point> positions = new LinkedList<Point>();
        FindOnMapAllRectanglesOfType(level, type, size, positions);
        if (positions.size() == 0) 
        {
            return false;
        }

        // get position of Random rectangle 
        int rnd = Random(positions.size());
        pos = positions.get(rnd);

        return true;
    }
	
	private static void FindOnMapAllRectanglesOfType(int[][] level, int type, Point size, LinkedList<Point> positions ) 
	{
        int N_x = level.length;
        int N_y = level[0].length;
		int[][] good_points;
        good_points = new int[N_x][N_y];
        
        // count horizontals

        for (int y = 0; y < N_y; ++y) 
        {
            int horizontal_count = 0;
            for (int x = 0; x < N_x; ++x) 
            {
                if (level[x][y] == type) 
                {
                    horizontal_count++;
                } 
                else 
                {
                    horizontal_count = 0;
                }

                if (horizontal_count == size.x) 
                {
                    good_points[x-size.x + 1][y] = 1;
                    horizontal_count--;
                }
            }
        }

        // count verticals

        for (int x = 0; x < N_x; ++x) 
        {
            int vertical_count = 0;
            for (int y = 0; y < N_y; ++y) 
            {
                if (good_points[x][y] == 1) 
                {
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
	
	private static void ConnectClosestRooms(int[][] level, boolean with_doors, boolean straight_connections) 
	{
		int N_x = level.length;
        int N_y = level[0].length;
		
        FillDisconnectedRoomsWithDifferentValues(level);
        LinkedList<LinkedList<Point>> rooms = new LinkedList<LinkedList<Point>>();
        
        for (int y = 0; y < N_y; ++y) 
        {
            for (int x = 0; x < N_x; ++x) 
            {
                if (level[x][y] != LevelElementWall_value) 
                {
                    if (level[x][y] >= rooms.size()) 
                    {
                        rooms.addLast(new LinkedList<Point>());
                    }

                    if (CountNeighboursOfType(level, LevelElementWall_value, new Point(x, y), false) > 0) // only border cells without diagonals
                    {
                        rooms.get(level[x][y]).addLast(new Point(x, y));
                    }
                }
            }
        }

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
                    to_connect_b = Random(rooms.size());
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
	
	private static int FillDisconnectedRoomsWithDifferentValues(int[][] level) 
	{
		int N_x = level.length;
		int N_y = level[0].length;
        for (int y = 0; y < N_y; ++y) 
        {
            for (int x = 0; x < N_x; ++x) 
            {
                if (level[x][y] == LevelElementRoom) 
                {
                    level[x][y] = LevelElementRoom_value;
                } 
                else if (level[x][y] == LevelElementWall) 
                {
                    level[x][y] = LevelElementWall_value;
                }
            }
        }

        int room_number = 0;

        for (int y = 0; y < N_y; ++y) 
        {
            for (int x = 0; x < N_x; ++x) 
            {
                if (level[x][y] == LevelElementRoom_value) 
                {
                    FloodFill(level, new Point(x, y), room_number++);
                }
            }
        }
        return room_number;
    }
	
    // default FloodFill
    private static boolean FloodFill(int[][] level, Point position, int value) 
    {
        return FloodFill(level, position, value, true, 0, new Point(-1, -1));
    }
	
	private static boolean FloodFill(int[][] level, Point position, int value, boolean diagonal, int gradient, Point end) 
	{
		int N_x = level.length;
		int N_y = level[0].length;
        // flood fill room
        int area_value = level[position.x][position.y];
        level[position.x][position.y] = value;

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

            int this_value = level[pos_x][pos_y];

            if (pos_x > 0) 
            {
                if (level[pos_x-1][pos_y] == area_value) 
                {
                    level[pos_x-1][pos_y] = this_value + gradient;
                    positions.addLast(new Point(pos_x - 1, pos_y));
                }
            }

            if (pos_x < N_x-1) 
            {
                if (level[pos_x+1][pos_y] == area_value) 
                {
                    level[pos_x+1][pos_y] = this_value + gradient;
                    positions.addLast(new Point(pos_x + 1, pos_y));
                }
            }

            if (pos_y > 0) 
            {
                if (level[pos_x][pos_y-1] == area_value) 
                {
                    level[pos_x][pos_y-1] = this_value + gradient;
                    positions.addLast(new Point(pos_x, pos_y - 1));
                }
            }

            if (pos_y < N_y-1) 
            {
                if (level[pos_x][pos_y+1] == area_value) 
                {
                    level[pos_x][pos_y+1] = this_value + gradient;
                    positions.addLast(new Point(pos_x, pos_y + 1));
                }
            }

            if (diagonal) 
            {
                if (pos_x > 0 && pos_y > 0) 
                {
                    if (level[pos_x-1][pos_y-1] == area_value) 
                    {
                        level[pos_x-1][pos_y-1] = this_value + gradient;
                        positions.addLast(new Point(pos_x - 1, pos_y - 1));
                    }
                }

                if (pos_x < N_x-1 && pos_y < N_y-1) 
                {
                    if (level[pos_x+1][pos_y+1] == area_value) 
                    {
                        level[pos_x+1][pos_y+1] = this_value + gradient;
                        positions.addLast(new Point(pos_x + 1, pos_y + 1));
                    }
                }

                if (pos_x < N_x-1 && pos_y > 0) 
                {
                    if (level[pos_x+1][pos_y-1] == area_value) 
                    {
                        level[pos_x+1][pos_y-1] = this_value + gradient;
                        positions.addLast(new Point(pos_x + 1, pos_y - 1));
                    }
                }

                if (pos_x > 0 && pos_y < N_y-1) 
                {
                    if (level[pos_x-1][pos_y+1] == area_value) 
                    {
                        level[pos_x-1][pos_y+1] = this_value + gradient;
                        positions.addLast(new Point(pos_x - 1, pos_y + 1));
                    }
                }
            }

            m = positions.remove(i);
        }

        return true;
    }
	
	// default CountNeighboursOfType
    private static int CountNeighboursOfType(int[][] level, int type, Point pos) {
        return CountNeighboursOfType(level, type, pos, true);
    }
    
    private static int CountNeighboursOfType(int[][] level, int type, Point pos, boolean diagonal) 
    {
    	int N_x = level.length;
    	int N_y = level[0].length;
        int neighbours = 0;
        if (pos.y > 0) {
            if (level[pos.x][pos.y-1] == type) // N
            {
                neighbours++;
            }
        }

        if (pos.x < N_x-1) 
        {
            if (level[pos.x+1][pos.y] == type) // E
            {
                neighbours++;
            }
        }

        if (pos.x > 0 && pos.y < N_y-1) 
        {
            if (level[pos.x][pos.y+1] == type) // S
            {
                neighbours++;
            }
        }

        if (pos.x > 0 && pos.y > 0) 
        {
            if (level[pos.x-1][pos.y] == type) // W
            {
                neighbours++;
            }
        }

        if (diagonal) {
            if (pos.x > 0 && pos.y > 0) {
                if (level[pos.x-1][pos.y-1] == type) // NW
                {
                    neighbours++;
                }
            }

            if (pos.x < (int) N_x - 1 && pos.y > 0) {
                if (level[pos.x+1][pos.y-1] == type) // NE
                {
                    neighbours++;
                }
            }

            if (pos.x < N_x-1 && pos.y < N_y-1) // SE
            {
                if (level[pos.x+1][pos.y+1] == type) 
                {
                    neighbours++;
                }
            }


            if (pos.x > 0 && pos.y < N_y-1) 
            {
                if (level[pos.x-1][pos.y+1] == type) // SW
                {
                    neighbours++;
                }
            }
        }

        return neighbours;
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
    
    public static double Distance(int x1, int y1, int x2, int y2) {
        int dX = x2 - x1;
        int dY = y2 - y1;
        return java.lang.Math.sqrt(dX * dX + dY * dY);
    }
    public static double Distance(Point p1, Point p2) {
        return Distance(p1.x, p1.y, p2.x, p2.y);
    }
    
    private static boolean CoinToss() 
    {
        return Random(2) != 0;
    }
    
 // default AddCoridor
    private static boolean AddCorridor(int[][] level, int start_x1, int start_y1, int start_x2, int start_y2) 
    {
        return AddCorridor(level, start_x1, start_y1, start_x2, start_y2, false);
    }
    
    private static boolean AddCorridor(int[][] level, int start_x1, int start_y1, int start_x2, int start_y2, boolean straight) 
    {	
    	int N_x = level.length;
    	int N_y = level[0].length;
    	
    	if(start_x1 < 0 || start_x1 >= N_x || start_y1 < 0 || start_y1 >= N_y)
    	{
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

            if (level[x1][y1] == LevelElementWall_value) 
            {
                level[x1][y1] = LevelElementCorridor_value;
            }
            if (level[x2][y2] == LevelElementWall_value) 
            {
                level[x2][y2] = LevelElementCorridor_value;
            }

            // connect corridors if on the same level
            if (x1 == x2) 
            {
                while (y1 != y2) 
                {
                    y1 += dir_y;
                    if (level[x1][y1] == LevelElementWall_value) 
                    {
                        level[x1][y1] = LevelElementCorridor_value;
                    }
                }
                if (level[x1][y1] == LevelElementWall_value) 
                {
                    level[x1][y1] = LevelElementCorridor_value;
                }
                return true;
            }
            if (y1 == y2) 
            {
                while (x1 != x2) 
                {
                    x1 += dir_x;
                    if (level[x1][y1] == LevelElementWall_value) 
                    {
                        level[x1][y1] = LevelElementCorridor_value;
                    }
                }
                if (level[x1][y1] == LevelElementWall_value) 
                {
                    level[x1][y1] = LevelElementCorridor_value;
                }
                return true;
            }
        }
        //return true;
    }
    
    private static int Random(int value) 
    {
        return RNG.getInt(value);
    }
    
    private static void ConvertValuesToTiles(int[][] level) 
    {
    	int N_x = level.length;
    	int N_y = level[0].length;
    	
        for (int y = 0; y < N_y; ++y) 
        {
            for (int x = 0; x < N_x; ++x) 
            {
                if (level[x][y] == LevelElementCorridor_value) 
                {
                    level[x][y] = LevelElementCorridor;
                } 
                else if (level[x][y] == LevelElementWall_value) 
                {
                    level[x][y] = LevelElementWall;
                } 
                else 
                {
                    level[x][y] = LevelElementRoom;
                }
            }
        }
    }
    
    private static void AddDoors(int[][] level, float door_probability, float open_probability) 
    {
    	int N_x = level.length;
    	int N_y = level[0].length;
    	
        for (int x = 0; x < N_x; ++x) 
        {
            for (int y = 0; y < N_y; ++y) 
            {
                Point pos = new Point(x, y);
                int room_cells = CountNeighboursOfType(level, LevelElementRoom, pos);
                int corridor_cells = CountNeighboursOfType(level, LevelElementCorridor, pos);
                int open_door_cells = CountNeighboursOfType(level, LevelElementDoorOpen, pos);
                int close_door_cells = CountNeighboursOfType(level, LevelElementDoorClose, pos);
                int door_cells = open_door_cells + close_door_cells;

                if (level[x][y] == LevelElementCorridor) 
                {
                    if ((corridor_cells == 1 && door_cells == 0 && room_cells > 0 && room_cells < 4) ||
                            (corridor_cells == 0 && door_cells == 0)) 
                    {
                        float exist = ((float) Random(1000)) / 1000;
                        if (exist < door_probability) {
                            float is_open = ((float) Random(1000)) / 1000;
                            if (is_open < open_probability) 
                            {
                                level[x][y] = LevelElementDoorOpen;
                            } 
                            else 
                            {
                                level[x][y] = LevelElementDoorClose;
                            }
                        }
                    }
                }
            }
        }
    }
}
