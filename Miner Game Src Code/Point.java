
/**
 * Immutable object representing a point on the VirtualWorld grid.
 */
final class Point
{
    private final int x;
    private final int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString()
    {
        return "Point(" + x + "," + y + ")";
    }

    public boolean equals(Object other)
    {
        if (other instanceof Point) {
            Point op = (Point) other;
            return x == op.x && y == op.y;
        } else {
            return false;
        }
    }

    public int hashCode()
    {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }
    public boolean adjacent(Point p2)
    {
        return (this.getX() == p2.getX() && Math.abs(this.getY() - p2.getY()) == 1)
            || (this.getY() == p2.getY() && Math.abs(this.getX() - p2.getX()) == 1);
    }

    public int distanceSquared(Point p2)
    {
        int deltaX = this.getX() - p2.getX();
        int deltaY = this.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }
}
