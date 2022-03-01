I had to design an algorithm based on the following scenario:

"Mariko and Stefano return from their foray in the damp Cascadian forests with full baskets of what they at first believe to be delicious porcini mushrooms. Alas, upon closer examination they realize they have mixed together two different but similar species, Boletus edulis (the choice edible) and Tylopilus felleus (unpalatable due to its strong bitter taste but luckily not poisonous). They’d like to separate the basket of n mushrooms into two piles, one for each species. They each pick pairs of specimens i and j and carefully note their similarities and differences. When they are confident, they can decide to call the pair (i, j) the “same” or “different” species or they can pass on that pair and conclude that the pair is “ambiguous.” 

They do this for a while until they reach a collection of m judgements (either “same” or “different”) for pairs of fungal fruiting bodies. Yet Mariko and Stefano have a dilemma: They would like to decide whether the m judgements are consistent or not. In other words, can they label the mushrooms as either edible or not so that each pair (i, j) judged “same” share the same label and those judged “different” have different labels?" 

My task was to create an algorithm with the running time O(m + n) that determined if the m judgements were consistent. Also, I had to assume their judgements were consistent and they didn’t make any mistakes. Last, I had to determine the minimum number of judgements m that they must make so that a single correct id of a porcini or bitter mushroom allows them to know the identity of all n mushrooms.

*The link to Github includes a pdf of the project and a README.md. 
      Analysis of Algorithms -> Mushroom Algorithm
