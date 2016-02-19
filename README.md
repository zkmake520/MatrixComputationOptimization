# MatrixComputationOptimization
Explore various techniques to speed up matrix computation

## Comparasion between 3 different ways
1. Main thread compute the whole matrix directly
2. Create several worker threads to compute each part of the matrix
3. Mutli worker threads and instead of computing the matrix line by line, I try to compute the matrix block by block.

###Results are stored in Result file, we can find that the speed can by significantly improved by using multithreading , but the method I used to improve the cache hit doesn't work fine
