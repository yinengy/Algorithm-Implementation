#include <vector>
#include <unordered_map>
#include <string>
#include <algorithm>
#include <iostream>

using namespace std;

class Trie {
public:
    Trie() : root(new node('0')) {}

    ~Trie() {
        delete root;
    }

    // insert a word to the tree, and add index to the node of the word
    // O(M), M is the length of input string
    void insert(string & input, int index) {
        int count = 0;
        node * trav = root;
        for (auto s : input) {
            auto found = trav->child.find(s);
            if (found != trav->child.end()) {
                trav = found->second;
            }
            else {
                trav->child[s] = new node(s);
                found = trav->child.find(s);
                trav = found->second;
            }

            count++;

            if (count == input.size()) {
                trav->wordIndex.push_back(index);
            }
        }
    }

    // find words that has the given prefixes and store index to result
    // search prefixes take O(M), find result takes O(MN) in the worest case
    void findprefixes(string & input, vector<int> & result) {
        int count = 0;
        node * trav = root;
        for (auto s : input) {
            auto found = trav->child.find(s);
            if (found != trav->child.end()) {
                trav = found->second;
            }
            else {
                return; // empty
            }

            count++;

            if (count == input.size()) {
                findchild(trav, result);
                return;
            }
        }
    }
private:
    // node in the tree
    class node {
    public:
        node(char chr) : chr(chr) {}

        node() : chr('0') {}

        ~node() {
            for (auto & i : child) {
                delete i.second;
            }
        }

        char chr;
        unordered_map<char, node *> child;
        vector<int> wordIndex;
    };

    node * root;

    // preorder traverse
    void findchild(node * trav, vector<int> & result) {
        for (auto index : trav->wordIndex) {
            result.push_back(index);
        }

        if (trav->child.size() == 0) {
            return;
        }

        for (auto & n : trav->child) {
            findchild(n.second, result);
        }

    }
};

// replace words in the sentence to its prefixes
vector<string> replace_words(const vector<string> & prefixes, const vector<string> & sentence) {
    Trie dictionary_tree;

    vector<string> new_sentence = sentence;

    // build trie, O(NM), N is then number of words, M is average length
    for (int i = 0; i < new_sentence.size(); i++) {
        dictionary_tree.insert(new_sentence[i], i);
    }

    vector<string> sorted_prefixes = prefixes;

    sort(sorted_prefixes.begin(), sorted_prefixes.end(),
        [](const string & a, const string & b) -> bool
    {
        return a.size() > b.size();
    }); // sort by size of prefixes in desending order, 
    // so will finally replace by shortest prefixes in next step

    // search and replace words
    // O(PNM) in worest case, O(PM) in best case
    for (int i = 0; i < sorted_prefixes.size(); i++) {
        vector<int> result;
        dictionary_tree.findprefixes(sorted_prefixes[i], result);
        for (int index : result) {
            new_sentence[index] = sorted_prefixes[i];
        }
    }

    return new_sentence;
}

int main() { //test
    vector<string> temp;
    temp = replace_words({ "cat", "bat", "rat" }, { "the", "cattle", "was", "rattled", "by", "the", "battery" });

    for (auto s : temp) {
        cout << s << endl;
    }
}