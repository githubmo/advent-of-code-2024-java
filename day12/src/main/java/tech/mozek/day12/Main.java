package tech.mozek.day12;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import tech.mozek.ReadInput;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Stream;
//
//public class Main {
//    private static final Logger logger = LoggerFactory.getLogger(Main.class);
//
//    public static void main(String[] args) {
//        try {
//            logger.info("Part 1 solution to sample is {}", part1(ReadInput.readSampleInput()));
//            logger.info("Part 1 solution to input is {}", part1(ReadInput.readInput()));
//            logger.info("Part 2 solution to sample is {}", part2(ReadInput.readSampleInput()));
//            logger.info("Part 2 solution to input is {}", part2(ReadInput.readInput()));
//        } catch (IOException e) {
//            logger.error("Error reading input", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static HashSet<HashSet<Point>> regions = new HashSet<HashSet<Point>>();
//
//    private static void generateRegions(Stream<String> input) {
//        regions.clear();
//        String[][] map = input.map(s -> s.split("")).toArray(String[][]::new);
//        for (int i = 0; i < map.length; i++) {
//            for (int j = 0; j < map[i].length; j++) {
//                addToRegions(new Point(i, j, map[i][j]));
//            }
//        }
//    }
//
//    public static long part1(Stream<String> input) throws IOException {
//        generateRegions(input);
//        return regions.stream().map(r -> {
//            var area = r.size();
//            var perimiter = r.stream().parallel().map(p -> {
//                return 4 - r.stream().map(pp -> {
//                    var dx = Math.abs(p.i - pp.i);
//                    var dy = Math.abs(p.j - pp.j);
//                    if (dx + dy == 1){
//                        return 1;
//                    };
//                    return 0;
//                }).reduce(Integer::sum).orElseThrow();
//            }).reduce(Integer::sum).orElseThrow();
//            return perimiter * area;
//        }).reduce(Integer::sum).orElseThrow();
//    }
//
//    public static long part2(Stream<String> input) throws IOException {
//        generateRegions(input);
//
//        long price = 0;
//
//        for (HashSet<Point> region : regions) {
//            // Step 1: Get boundary points
//            HashSet<Point> boundaryPoints = getBoundaryPoints(region);
//
//            // Step 2: Order boundary points
//            List<Point> orderedBoundary = orderBoundaryPoints(boundaryPoints);
//
//            var corners = countCorners(new HashSet<>(orderedBoundary));
//            if (region.size() == 1) corners = 4;
//            if (region.size() == 2) corners = 4;
//
//            // Step 3: Count corners
//            price += (long) corners * region.size();
//        }
//
//        return price;
//    }
//
//    private static void addToRegions(Point p) {
//        var foundRegions = new HashSet<HashSet<Point>>(){};
//        for (var region : regions) {
//            var isInRegion = region.stream().anyMatch(pp -> {
//                if (pp.equals(p)) return true;
//                var dx = Math.abs(p.i - pp.i);
//                var dy = Math.abs(p.j - pp.j);
//                if (dx + dy == 1 && pp.s.equals(p.s)){
//                    return true;
//                };
//                return false;
//            });
//            if (isInRegion) {
//                region.add(p);
//                foundRegions.add(region);
//            }
//        }
//        if (foundRegions.size() > 1) {
//            regions.removeIf(r -> r.contains(p));
//            var newRegion = new HashSet<Point>();
//            foundRegions.forEach(newRegion::addAll);
//            regions.add(newRegion);
//        } else if (foundRegions.isEmpty()) {
//            var newRegion = new HashSet<Point>();
//            newRegion.add(p);
//            regions.add(newRegion);
//        }
//    }
//
//    private record Point(int i, int j, String s) {
//    }
//
//    private static HashSet<Point> getBoundaryPoints(HashSet<Point> region) {
//        HashSet<Point> boundaryPoints = new HashSet<>();
//
//        for (Point p : region) {
//            boolean isBoundary = false;
//
//            // Check the 4 neighbors of point (up, down, left, right)
//            for (int[] dir : new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
//                Point neighbor = new Point(p.i + dir[0], p.j + dir[1], p.s);
//                if (!region.contains(neighbor)) {
//                    isBoundary = true;
//                    break;
//                }
//            }
//
//            // If boundary point, add to the set
//            if (isBoundary) {
//                boundaryPoints.add(p);
//            }
//        }
//
//        return boundaryPoints;
//    }
//
//    private static List<Point> orderBoundaryPoints(HashSet<Point> boundaryPoints) {
//        if (boundaryPoints.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        // Step 1: Find the starting point (top-leftmost)
//        Point start = boundaryPoints.stream()
//                .min(Comparator.<Point>comparingInt(p -> p.i).thenComparingInt(p -> p.j))
//                .orElseThrow();
//
//        // Step 2: Sort the remaining points by angle with respect to the starting point
//        List<Point> sortedBoundary = new ArrayList<>(boundaryPoints);
//        sortedBoundary.sort((p1, p2) -> {
//            if (p1.equals(start)) return -1;
//            if (p2.equals(start)) return 1;
//
//            int dx1 = p1.i - start.i, dy1 = p1.j - start.j;
//            int dx2 = p2.i - start.i, dy2 = p2.j - start.j;
//
//            double angle1 = Math.atan2(dy1, dx1);
//            double angle2 = Math.atan2(dy2, dx2);
//
//            return Double.compare(angle1, angle2);
//        });
//
//        // Step 3: Ensure all boundary points are returned in sorted order
//        return sortedBoundary;
//    }
//
//    private static int countCorners(HashSet<Point> region) {
//        // Set to store unique edges
//        HashSet<String> uniqueEdges = new HashSet<>();
//
//        // Step 1: Add all edges of each square
//        for (Point p : region) {
//            // Generate 4 edges of the current square
//            String[] edges = new String[] {
//                    edgeKey(p.i, p.j, p.i, p.j + 1), // Top edge
//                    edgeKey(p.i, p.j + 1, p.i + 1, p.j + 1), // Right edge
//                    edgeKey(p.i + 1, p.j + 1, p.i + 1, p.j), // Bottom edge
//                    edgeKey(p.i + 1, p.j, p.i, p.j) // Left edge
//            };
//
//            // Add or remove edges from the set (shared edges cancel out)
//            for (String edge : edges) {
//                if (uniqueEdges.contains(edge)) {
//                    uniqueEdges.remove(edge); // Remove if already there (shared edge)
//                } else {
//                    uniqueEdges.add(edge); // Otherwise, add it
//                }
//            }
//        }
//
//        // Step 2: Traverse the unique edges to count corners
//
//        // Convert the set of edges into a list of parsed edges
//        List<int[]> edgeList = new ArrayList<>();
//        for (String edge : uniqueEdges) {
//            edgeList.add(parseEdgeKey(edge));
//        }
//
//        // Find the starting edge (arbitrarily pick an edge in the cycle)
//        int corners = 0;
//
//        // Traverse each edge to detect turns
//        for (int i = 0; i < edgeList.size(); i++) {
//            // Current and next edges
//            int[] currentEdge = edgeList.get(i);
//            int[] nextEdge = edgeList.get((i + 1) % edgeList.size());
//
//            // Calculate direction vectors for the two edges
//            int dx1 = currentEdge[2] - currentEdge[0]; // x2 - x1
//            int dy1 = currentEdge[3] - currentEdge[1]; // y2 - y1
//            int dx2 = nextEdge[2] - nextEdge[0]; // x2 - x1 (for next edge)
//            int dy2 = nextEdge[3] - nextEdge[1]; // y2 - y1
//
//            // Check if the direction changes (non-parallel edges)
//            if (dx1 != dx2 || dy1 != dy2) {
//                corners++;
//            }
//        }
//
//        return corners;
//    }
//
//    // Helper to generate a unique key for an edge (order-independent)
//    private static String edgeKey(int x1, int y1, int x2, int y2) {
//        if (x1 < x2 || (x1 == x2 && y1 < y2)) {
//            return x1 + "," + y1 + "-" + x2 + "," + y2;
//        } else {
//            return x2 + "," + y2 + "-" + x1 + "," + y1;
//        }
//    }
//
//    // Helper to parse edge keys back into coordinates
//    private static int[] parseEdgeKey(String edgeKey) {
//        String[] parts = edgeKey.split("[-,]");
//        return new int[] {
//                Integer.parseInt(parts[0]), // x1
//                Integer.parseInt(parts[1]), // y1
//                Integer.parseInt(parts[2]), // x2
//                Integer.parseInt(parts[3])  // y2
//        };
//    }
//
//}

import tech.mozek.ReadInput;

import java.io.*;
import java.util.*;
import java.util.stream.*;

class Main {
    public static void main(String[] args) throws IOException {
        new Puzzle().solve();
    }
}

class Puzzle {

    private final Grid grid;

    Puzzle() throws IOException {
//        try (var input = Objects.requireNonNull(getClass().getResourceAsStream("input.text"))) {
//
//        }
        var input = ReadInput.readInput();
        grid = Grid.from(input);
    }

    void solve() {
        var regions = grid.regions();
        System.out.println(regions.stream().mapToInt(Region::price).sum());
        System.out.println(regions.stream().mapToLong(Region::betterPrice).sum());
    }
}

record Coordinate(int x, int y) implements Comparable<Coordinate> {
    Collection<Coordinate> neighbours() {
        return Set.of(
                new Coordinate(x + 1, y),
                new Coordinate(x, y + 1),
                new Coordinate(x - 1, y),
                new Coordinate(x, y - 1)
        );
    }

    Collection<Coordinate> edges() {
        return Set.of(
                new Coordinate(x, y),
                new Coordinate(x, y + 1),
                new Coordinate(x + 1, y),
                new Coordinate(x + 1, y + 1)
        );
    }

    boolean isNeighbor(Coordinate other) {
        return x == other.x || y == other.y;
    }

    @Override
    public int compareTo(Coordinate o) {
        return y == o.y ? Integer.compare(x, o.x) : Integer.compare(y, o.y);
    }
}

record Region(Map<Coordinate, Integer> plots) {
    int area() {
        return plots.size();
    }

    int perimeter() {
        return plots.values().stream().mapToInt(i -> i).sum();
    }

    int price() {
        return area() * perimeter();
    }

    long edges() {
        Map<Coordinate, SortedSet<Coordinate>> edges = new HashMap<>();
        for (Coordinate coordinate : plots.keySet()) {
            for (Coordinate edge : coordinate.edges()) {
                edges.computeIfAbsent(edge, c -> new TreeSet<>());
                edges.get(edge).add(coordinate);
            }

        }
        long hidden = 2 * edges.values().stream().filter(s -> s.size() == 2 && !s.first().isNeighbor(s.last())).count();
        return edges.values().stream().filter(s -> (s.size() & 1) == 1).count() + hidden;
    }

    long betterPrice() {
        return area() * edges();
    }
}

record Grid(Map<Coordinate, Character> plots) {
    static Grid from(Stream<String> lines) {
        Map<Coordinate, Character> plots = new HashMap<>();
        int y = 0;
        for (String line : lines.toList()) {
            int x = 0;
            for (char c : line.toCharArray()) {
                plots.put(new Coordinate(x, y), c);
                x++;
            }
            y++;
        }
        return new Grid(plots);
    }

    Region region(Coordinate coordinate, Set<Coordinate> visited) {
        var thisPlant = plots.get(coordinate);
        Map<Coordinate, Integer> bits = new HashMap<>();
        Deque<Coordinate> queue = new ArrayDeque<>();
        queue.addLast(coordinate);
        while (!queue.isEmpty()) {
            if (queue.size() > plots.size()) {
                throw new IllegalStateException();
            }
            Coordinate c = queue.removeFirst();
            visited.add(c);
            int fences = 4;
            for (Coordinate neighbour : c.neighbours()) {
                var otherPlant = plots.get(neighbour);
                if (Objects.equals(otherPlant, thisPlant)) {
                    --fences;
                    if (!visited.contains(neighbour)) {
                        if (!queue.contains(neighbour)) {
                            queue.addLast(neighbour);
                        }
                    }
                }
            }
            bits.put(c, fences);
        }
        return new Region(bits);
    }

    List<Region> regions() {
        List<Region> regions = new ArrayList<>();
        Set<Coordinate> visited = new HashSet<>();
        for (Coordinate coordinate : plots.keySet()) {
            if (!visited.contains(coordinate)) {
                regions.add(region(coordinate, visited));
            }
        }
        return regions;
    }
}
