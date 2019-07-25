
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class AStarPathingStrategy implements PathingStrategy
{
    private DebugGrid grid;
    @Override
    public List<Point> computePath(final Point start, final Point end,
                            Predicate<Point> canPassThrough,
                            Function<Point, List<Point>> potentialNeighbors,
                            ToIntBiFunction<Point, Point> stepsFromTo)
    {
        Set<Point> closedSet = new HashSet<Point>();
        Set<Point> openSet = new HashSet<Point>();
        openSet.add(start);
        Map<Point, Point> cameFrom = new HashMap<Point, Point>();
        Map<Point, Double> gScore = new HashMap<Point, Double>();
        gScore.put(start, 0.0);
        Map<Point, Double> fScore = new HashMap<Point, Double>();
        fScore.put(start, costEstimate(start, end, stepsFromTo.applyAsInt(start, end)));
        while (openSet.size() >0)
        {
            paintGrid(openSet, closedSet);
            //System.out.println(openSet.size());
            Point current = new Point(0,0);
            double min = Double.POSITIVE_INFINITY;
            for (Point p: openSet)
            {
                //System.out.println(p);
                if  (fScore.get(p) < min) {
                    min = fScore.get(p);
                    current = p;
                }
            }
            if (current.adjacent(end)) {
                return reconstruct_path(cameFrom, current);
            }
            openSet.remove(current);
            closedSet.add(current);
            for(Point neighbor: potentialNeighbors.apply(current))
            {
                if (!canPassThrough.test(neighbor))
                {
                    continue;
                }
                if (closedSet.contains(neighbor))
                {
                    continue;
                }

                if (!openSet.contains(neighbor))
                {
                    fScore.put(neighbor, Double.POSITIVE_INFINITY);
                    gScore.put(neighbor, Double.POSITIVE_INFINITY);
                    openSet.add(neighbor);
                }
                //System.out.println(gScore.get(current));
                double tentative_gScore = gScore.get(current) + stepsFromTo.applyAsInt(current, neighbor);
                //System.out.println(gScore.get(neighbor));
                if (tentative_gScore >= gScore.get(neighbor))
                {
                    continue;
                }
                //System.out.println("hello");
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentative_gScore);
                fScore.put(neighbor, gScore.get(neighbor) + costEstimate(neighbor, end, stepsFromTo.applyAsInt(neighbor, end)));
            }
        }
        return new ArrayList<Point>();
    }

    protected double costEstimate(Point p1, Point p2, int stepsFromTo)
    {
        //double distance = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p2.getY() - p1.getY(),2));
        return stepsFromTo * 1.000000001;
    }
    @Override
    public void setDebugGrid(DebugGrid grid)
    {
        this.grid = grid;
    }
    public List<Point> reconstruct_path(Map<Point, Point> cameFrom, Point current)
    {
        List<Point> total_path = new ArrayList<Point>();
        total_path.add(current);
        while(cameFrom.keySet().contains(current))
        {
            //System.out.println("dope");
            current = cameFrom.get(current);
            total_path.add(0, current);
        }
        total_path.remove(0);
        return total_path;
    }

    public void paintGrid(Set<Point> os, Set<Point> cs)
    {
        if (grid != null && DebugGrid.ENABLED)
        {
            for (Point p: os)
            {
                grid.setGridValue(p, DebugGrid.OPEN_SET_TILE);
            }
            for (Point p: cs)
            {
                grid.setGridValue(p, DebugGrid.CLOSED_SET_TILE);
            }
            grid.showFrame();
        }
    }
}
