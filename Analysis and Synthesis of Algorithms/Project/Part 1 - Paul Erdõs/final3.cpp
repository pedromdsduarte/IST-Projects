/**********************************
 * Grupo #096
 * 
 * Pedro Duarte - 78328
 * Goncalo Fialho - 79112
 * 
 * *******************************/
 
#include <iostream>
#include <queue>
#include <vector>



enum color {
    WHITE,
    GREY,
    BLACK
};


class Graph {
    private:
        int nodes; //numero de vertices do grafo
        std::vector<std::vector<int> > links; //lista de adjacencia
    public:
        Graph(int size);
        ~Graph();
        void addLink(int u, int v);
        void BFS(int start);
};

Graph::Graph(int size) {
    nodes = size;
    links.resize(nodes);
}


Graph::~Graph() {
    for(int i = 0; i < nodes; i++)
        links[i].clear();
    links.clear();
}


void Graph::addLink(int u, int v) {
    //cria arco entre os vertices u e v (nao dirigido)
    
    links[u-1].push_back(v-1);
    links[v-1].push_back(u-1);
}


void Graph::BFS(int start) {
    //realiza uma procura BFS no grafo, com inicio no vertice 'start'
    
    int M = 0;  //maior valor do numero de Erdos
    std::queue<int> Q;
    int* color = new int[nodes];
    int* distance = new int[nodes];
    
    for(int i = 0; i < nodes; i++) {
        //inicializa o array das cores e das distancias dos vertices a origem
        //color[0] corresponde a cor do vertice 1...
        
        color[i] = WHITE;
        distance[i] = -1;
    }
    
    //o vertice 'start' tem distancia 0 de si proprio
    distance[start-1] = 0;
    color[start-1] = GREY;
    
    Q.push(start-1);

    
    while(!Q.empty()) {
        int node = Q.front();
        Q.pop();

        for(std::vector<int>::iterator it = links[node].begin(); it != links[node].end(); it++) {
            if (color[*it] == WHITE) {
                color[*it] = GREY;
                distance[*it] = distance[node] + 1;
                
                if(distance[*it] > M)
                    M = distance[*it];
                Q.push(*it);
            }
        }
        color[node] = BLACK;
        
    }


    std::cout << M << std::endl;
    
    
    int cnt[M+1];
    for(int i = 0; i <= M; i++)
        cnt[i] = 0;
    
    //conta o numero de vertices com distancia 1, 2, ...    
    for(int j = 0; j < nodes; j++) {
        cnt[distance[j]]++;
    }
    
    
    for(int i = 1; i <= M; i++) {
        std::cout << cnt[i] << std::endl;
    }

    delete []color;
    delete []distance;
}

/******************************************************************************/

int main() {
    
    
    int N, C, start;
    int u, v;
    
    std::cin >> N >> C >> start;
    
    Graph g(N); //inicializa o grafo com N vertices
    
    for(int i = 0; i < C; i++) {
        //liga os vertices consoante o input proveniente do stdin
        std::cin >> u >> v;
        g.addLink(u,v);
    }

    
    g.BFS(start); //procura BFS sobre o grafo 'g'
    return 0;
}