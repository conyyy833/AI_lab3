**Item-based collaborative filtering algorithm [ItemCF]**

**Case background:** 
also recommend hospitals to target users based on the data of commented hospitals. 
Note that in general, users choose hospitals according to their own medical needs, so in the case of a sufficient user base, if most users choose hospital 1 and also choose hospital 2, then it can be said that hospital 12 is similar. However, the scoring of the hospital is more based on the quality of the departments of the hospital and the effectiveness of the treatment. 
Therefore, in this implementation, all non-zero scores in the UserCF implementation are set to 1.

**Idea steps:**
1. Calculate the similarity between items (also using the Jaccard similarity measurement algorithm);
2. Generate a recommendation list for the user based on the similarity between the items and the user's historical behavior.