import java.util.Scanner;

public class MapRoute {
    private static class MyQu{
        private Vertex[] heap;
        private int cap;
        private int count;
    }

    private static void insert(MyQu qu, Vertex ptr){
        int i = qu.count;
        qu.count++;
        qu.heap[i] = ptr;
        while (i > 0 && qu.heap[(i-1)/2].dist > qu.heap[i].dist){
            Vertex swap = qu.heap[(i - 1)/2];
            qu.heap[(i - 1)/2] = qu.heap[i];
            qu.heap[i] = swap;
            qu.heap[i].index = i;
            i = (i - 1)/2;
        }
        qu.heap[i].index = i;
    }

    private static Vertex extractMin(MyQu qu){
        Vertex v = qu.heap[0];
        qu.count--;
        if (qu.count > 0){
            qu.heap[0] = qu.heap[qu.count];
            qu.heap[0].index = 0;
            qu.heap = heapify(qu);
        }
        return v;
    }

    private static Vertex[] heapify(MyQu qu){
        int i = 0;
        int n = qu.count;
        Vertex[] P = qu.heap;
        int l, r, j;
        for(;;)
        {
            l = 2*i + 1;
            r = l + 1;
            j = i;
            if (l < n && P[i].dist > P[l].dist)
                i = l;
            if (r < n && P[i].dist > P[r].dist)
                i = r;
            if (i == j)
                break;
            Vertex swap = P[i];
            P[i] = P[j];
            P[j] = swap;
            P[i].index = i;
            P[j].index = j;
        }
        return  P;
    }

    private static void decreaseKey(MyQu qu, Vertex ptr, int kk){
        int i = ptr.index;
        ptr.dist = kk;
        while (i>0 && qu.heap[(i-1)/2].dist > kk){
            Vertex swap = qu.heap[(i - 1)/2];
            qu.heap[(i - 1)/2] = qu.heap[i];
            qu.heap[i] = swap;
            qu.heap[i].index = i;
            i = (i - 1) / 2;
        }
        ptr.index = i;
    }

    private static class Vertex {
        private int mass;
        private int dist;
        private int index;
        private int i;
        private int j;
        private Vertex parent;
    }

    public static void dijstra(Vertex[][] matrix, int a, int b, int n){
        MyQu qu = new MyQu();
        qu.cap = n * n;
        qu.count = 0;
        qu.heap = new Vertex[n * n];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++){
                if(i == a && j == b)
                    matrix[i][j].dist = 0;
                else
                    matrix[i][j].dist = (int) Double.POSITIVE_INFINITY;
                matrix[i][j].parent = null;
                insert(qu, matrix[i][j]);
            }
        while(qu.count > 0){
            Vertex v = extractMin(qu);
            v.index = -1;
            Vertex[] healp = new Vertex[4];
            int ii = v.i;
            int jj = v.j;
            int cc = 0;
            if (ii>0) {
                healp[cc] = matrix[ii - 1][jj];
                cc++;
            }
            if (ii < n-1) {
                healp[cc] = matrix[ii + 1][jj];
                cc++;
            }
            if (jj > 0) {
                healp[cc] = matrix[ii][jj - 1];
                cc++;
            }
            if (jj < n-1){
                healp[cc] = matrix[ii][jj + 1];
                cc++;
            }
            for (int q = 0; q < cc; q++){
                Vertex u = healp[q];
                if (u.index != -1 && relax(v, u, u.mass))
                    decreaseKey(qu, u, u.dist);
            }
        }
    }

    private static boolean relax(Vertex u, Vertex v, int q){
        boolean f = (u.dist + q)< v.dist;
        if ((u.dist + q)< v.dist){
            v.dist = u.dist + q;
            v.parent = u;
        }
        return f;
    }

    public static void main(String[] args){
        Scanner a = new Scanner(System.in);
        int n = a.nextInt();
        Vertex[][] matrix = new Vertex[n][n];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++){
                matrix[i][j] = new Vertex();
                matrix[i][j].mass = a.nextInt();
                matrix[i][j].i = i;
                matrix[i][j].j = j;
            }
        dijstra(matrix, 0, 0, n);
        System.out.println(matrix[n-1][n-1].dist + matrix[0][0].mass);
    }
}
