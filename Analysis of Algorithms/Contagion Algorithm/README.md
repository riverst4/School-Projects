I had to design an algorithm based off the following scenario:

"You’re helping perform contact tracing of a potentially serious virus spreading through the local university.The university is big on data, so they’ve required all students to sign up for an app that tracks where they are at all times and can tell whether two people using the app are close enough for the virus to spread. There are n students S_1, S_2, . . . , Sn attending the university and you have access to a database of contact events given as triples (S_i, S_j , t_k) indicating that S_i and S_j were in contact at time t_k minutes.

Since the database collects contact events as they are recorded by the users’ phones, you can assume that the triples come in time sorted order. For simplicity, assume that each pair of students makes at most one contact during the time window of interest. The concept of contact is symmetric: S_i being in contact with S_j is the same as S_j being in contact with S_i. 

The campus health office would like to be able to answer the following kind of question: If a student Sa becomes infected at time x, could they have infected another student Sb by time y? We assume simple infection dynamics: If an infected student S_i has contact with another student S_j at time t_k, then student S_j becomes infected at time t_k + T , where T ≥ 0 is the incubation period. Once someone is infected they remain contagious for the rest of the period of interest. So whether or not S_b could have been infected depends on whether there were a sequence of contact events properly spaced in time so that the virus could spread from S_a.

An example with n = 4 and T = 2 and contact data (S_1, S_2, 4), (S_2, S_4, 8), (S_3, S_4, 10), (S_1, S_4, 12) could have started with S1 infected at time 2, leading to S_2 becoming infected at time 6 (T = 2 time units after the contact event at time 4), then S_4 is infected at time 10, and S_3 infected at time 12. However, if T = 3 and S_1 is infective at time 1, then S_2 gets infected at time 7 and S_4 falls ill at time 11, but S_3 manages to avoid the sickness since their contact with S_4 occured too early."

The task that needed to be completed was to create an algorithm that decided if student S_a becomes infected at time x whether student Sb could have been
infected by time y. Lasly, I had to make sure the algorithm ran in O(m +n) amount of time.
