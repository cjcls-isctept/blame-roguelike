package su.msk.dunno.blame.map.gen;

import su.msk.dunno.blame.support.Point;

public class AntNest 
{
	static int LevelElementWall  = 0;
	static int LevelElementCorridor  = 1;
	static int LevelElementRoom  = 1;
	
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
            k = Math.random()*360 * 3.1419532 / 180;
            // position on ellipse by degree
            x1 = N_x/2 + N_x*Math.sin(k);
            y1 = N_y/2 + (N_y/2)*Math.cos(k);

            // object will move not too horizontal and not too vertival
            do {
                dx = Math.random()*100;
                dy = Math.random()*100;
            } while ((Math.abs(dx) < 10 && Math.abs(dy) < 10));
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
            for (y = 1; y < N_y - 1; y++) {
                for (x = 1; x < N_x - 1; x++) {
                    if ((x > N_x/2 - 10 && x < N_x/2 + 10 && y > N_y/2 - 5 && y < N_y/2 + 5) || level[x][y] == LevelElementWall) {
                        continue;
                    }

                    int neighbours = CountNeighboursOfType(level, LevelElementCorridor, new Point(x, y));

                    if (neighbours == 1) {
                        for (px = -1; px <= 1; px++) {
                            for (py = -1; py <= 1; py++) {
                                level[x+px][y+py] = LevelElementRoom;
                            }
                        }
                    }
                }
            }
        } // end of if (with_rooms)
        return level;
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
}
