/**********************************
 * Grupo #096
 *
 * Pedro Duarte - 78328
 * Goncalo Fialho - 79112
 *
 * *******************************/

#include <vector>
#include <cstdio>
#include <climits>

/*******************************************************/

void printResult(int* cycle, int* d, int V) {
    for(int i = 1; i <= V; i++) {
        if(d[i] == INT_MAX)
            printf("U\n");
        else if(cycle[i] == 1)
            printf("I\n");
        else
            printf("%d\n",d[i]);
    }
}

/*******************************************************/

class Edge {
    public:
        int src, dest, weight;
        Edge(int s, int d, int w) : src(s), dest(d), weight(w) {}
};

class Graph {
    public:
        int nodes; //numero de vertices do grafo
        std::vector<Edge*> edges; //vector que mantem os arcos do grafo
        std::vector<std::vector<int> > links; //lista de adjacencia
        Graph(int size, int E) : nodes(size), edges() { links.resize(size);}
        ~Graph();
        void addEdge(int u, int v, int w);
        void bellmanFord(int source);
    private:
        void mark(int v, int* cycle);
};



Graph::~Graph() {
    for(int i = 0; i < (int)edges.size(); i++)
        delete edges[i];
    for(int i = 0; i < nodes; i++)
        links[i].clear();
    links.clear();
}


void Graph::addEdge(int u, int v, int w) {
    //cria arco entre os vertices u e v, com peso w;
    //Edge edge(u,v,w);
    links[u-1].push_back(v-1);
    Edge* edge = new Edge(u,v,w);
    edges.push_back(edge);
}

/*******************************************************/

void Graph::mark(int v, int* cycle) {
    cycle[v] = 1;

    for(int i = 0; i < (int)links[v-1].size(); i++) {
        
        /*Marca todos os vertices adjacentes ao vertice v 
        que ainda nao foram marcados
        (isto e, aqueles que tem como predecessor o vertice v)*/
        
        if(cycle[links[v-1][i]+1] != 1) 
            mark(links[v-1][i]+1,cycle);    
    }
}

/*******************************************************/

void Graph::bellmanFord(int s) {
    int V = nodes;
    int E = (int)edges.size();
    int d[V+1];
    int done = 0;


    /*Inicializa os vertices a infinito e a source a 0*/
    for(int i = 0; i < V; i++) {
        d[i+1] = INT_MAX;
    }
    d[s] = 0;
    
    /*Inicializa o vector onde se vai marcar o ciclo*/
    int cycle[V+1];
    for(int j = 1; j <= V; j++) {
        cycle[j] = 0;
    }

    /*Relaxa todos os vertices, V-1 vezes*/
    for(int i = 1; i <= V-1; i++) {
        done = 1;
        for(int j = 0; j < E; j++) {
            int u = edges[j]->src;
            int v = edges[j]->dest;
            int w = edges[j]->weight;
            if(d[u] != INT_MAX && (d[v] > d[u] + w)) {
                d[v] = d[u] + w;
                done = 0;
                
            }
        }
        
        /*Se nao foi feito nenhum Relax na ultima iteracao, o algoritmo acaba
        (isto significa que nao ha ciclos de peso negativo)*/
        if(done == 1) {
            printResult(cycle,d,V);
            return;
        }
    }


    /*Verifica se ha ciclos de peso negativo
    (se ainda for possivel fazer Relax, entao existe um ciclo de peso negativo;
    todos os vertices que saem de v fazem parte do ciclo, incluindo v;
    por conseguinte, todos os que saem dos adjacentes de v tambem sao, e por ai
    adiante;)
    */
    for(int i = 0; i < E; i++) {
        int u = edges[i]->src;
        int v = edges[i]->dest;
        int w = edges[i]->weight;

        if(d[v] > d[u] + w && d[u] != INT_MAX) {
            d[v] = d[u] + w;
            mark(v, cycle);
        }
    }

    printResult(cycle,d,V);
}

/*******************************************************/

int main() {
    int N, C, S;
    int u, v, w;

    if(scanf("%d %d %d", &N, &C, &S) < 0)
        perror("scanf\n");
    Graph g(N, C);

    for(int i = 0; i < C; i++) {
        if(scanf("%d %d %d", &u, &v, &w) < 0)
            perror("scanf\n");
        g.addEdge(u,v,w);
    }
    
    g.bellmanFord(S);
    return 0;
}