# Java Chess Engine

![promo](https://github.com/cooper-ross/java-chess/assets/120236631/4f144503-8dc9-4a8e-96e7-9387afb21feb)


> Quick note: Try not to go over 6 in depth (or 9 on PCs), or you'll have to wait like 20 seconds! If you want, you can package it into a jar file so replit's 0.5 virtual CPU doesn't drag down the performance too much.

> #### Second quick note: The program is still unfinished! I have way more work to do! It should take me another 2 or 3 days to implement a endgame system for evaluation, and then I want to try some tablebase stuff for openings and 5 man tablebase lookups!
## Overview

- The board is represented by a 64x (single dimension) byte array.
- Move generation is based on a set of *idealistic precomputed moves* (meaning without considering blocked pieces or captures) for every square (for every possible piece), and then filters what can and can't be done on the board state.
- The precomputed data contains a 2D array for each square on the board, with the outer array being the directions, inner being the squares traveled in order.
- The search algorithm I picked was the most basic quintessential minimax, and then I added a simple alpha beta pruning method also. If you're interested, there's a [great video](https://www.youtube.com/watch?v=l-hh51ncgDI) by Sebastian Lague on this!
- UI was just some "borrowed" code from my tictactoe project earlier this term, and is quite simply just an array of 64 buttons with a custom icon for each relative to the piece occupying that square.
- If you're wondering why all the Java files are in the root directory, it's partly because of how replit compiles, and partly because it was required for ease-of-grading.

## Usage

There is a pretty simple UI for most actions, and the buttons are pretty self explanatory. You can make moves by clicking the piece, and then clicking it's destination (if legal).
If you want to use more complicated freatures, there is also a commandline interface for communicating with the engine, here's how to use it:
- Undo your last move (or the bot's last move) with the undo command:
> `undo`
- Find the best move for the current player, and optionally specify the search depth:
> `best 6`
- Find the best move for the current player at each depth up to the specified depth, and optionally specify the max search depth:
> `best_iterative 6`
- Find overall evaluation, and (again) optionally specify the search depth:
> `eval 4`
- Find shallow leaf evaluation (the base evaluation at the terminal/leaf nodes):
> `shallow`
- Load a position with fen notation like so:
 > `fen "rn3rk1/p5pp/2p5/3Ppb2/2q5/1Q6/PPPB2PP/R3K1NR b - - 0 1"`

## Challenges Faced

I faced a few "big" challenges, but a lot of really small ones. Most of the Challenges I faced were due to some of Java's odd formatting, or my refusal to use lists unless absolutely necessary. Most of these were technical issues in PrecomputedData, where I had to figure out how many possible moves a piece had before checking it's possible moves. I solved all of these with a bit of math, to turn the index into a row and column, and then I could just do simple subtraction to figure out the relative distance to the center.

The rest of the big challenges are all related to one thing: speed! With the least optimized version of my engine, it can search about 200 nodes a second (replit's processor does not help at all), which is not great. Alpha-beta pruning helps, but it doesn't speed up the time we need for each node, it just reduces the overall nodes. The slowest part at that time was the move generator, which has gone through a ton of changes, and can check about 1 node per 0.5ms, or 2k nodes per second, which is much nicer! There was a lot of small changes, but here are the biggest few: I changed it so it would check per move if the king wil be in check, and break early for sliding attacks. Castling, pawn moves, and searching the history are slow, so I avoid them as much as possible by making sure the easiest (and fastest) conditions to check came first, and the slower ones would only run if everything else was true. I also used a builder class to make move generation a bit cleaner looking, and suprisingly faster.

## Future Improvements

Here are some potential enhancements and features I'd like to add if I ever get around too it!
- Transposition table
- Killer moves
- Quiescent Search
- Search extensions
- Negascout
- Silent moves - sort by PSQT value

## Testing it out!

I have a few of test postions for the bot, to find mate, or the best move in any given situation:
* `3r2k1/p4ppp/b1pb1Q2/q7/8/1B3p2/PBPPNP1P/1R2K1R1 b - - 1 0` Mate in 4. Requires a depth of 6, so a search time of around 3000ms should work (if you are on PC and not replit, 500ms works).
* `rn3rk1/p5pp/2p5/3Ppb2/2q5/1Q6/PPPB2PP/R3K1NR b - - 0 1` Mate in 3. Requires a depth of 4, so should work within the default time (if you are on PC and not replit, 500ms works).
* `7r/p3ppk1/3p4/2p1P1Kp/2Pb4/3P1QPq/PP5P/R6R b - - 0 1` Mate in 2. Works on the default time (if you are on PC and not replit, 50ms works).
* `r2qk2r/1pp2ppp/p1np1n2/2b1p1B1/2B1P1b1/2NP1N1P/PPP2PP1/R2Q1RK1 b kq - 0 8` An agree with stockfish test, best move is bishop takes f3, so a search time of around 1000ms should work (if you are on PC and not replit, 300ms works).
* `8/8/7k/8/8/8/5q2/3B2RK b - - 0 1` Weird piece winning combo. Depth 4 is fine. Best move is king to h7, which leads to winning a piece a few moves later, so a search time of around 800ms should work (if you are on PC and not replit, 300ms works).

## Example console (800ms, PC)

```
Got to depth: 1 Searched Nodes: 1.0 Evaluated Nodes: 30.0 5742 0
Got to depth: 2 Searched Nodes: 31.0 Evaluated Nodes: 133.0 5742 3
Got to depth: 3 Searched Nodes: 185.0 Evaluated Nodes: 2860.0 6125 28
Got to depth: 4 Searched Nodes: 4628.0 Evaluated Nodes: 25212.0 6125 93
Got to depth: 5 Searched Nodes: 9967.0 Evaluated Nodes: 168733.0 5742 282
Got to depth: 6 Searched Nodes: 323097.0 Evaluated Nodes: 1351780.0 5742 695
```

## Example console (30s, Replit)

```
Got to depth: 1 Searched Nodes: 1.0 Evaluated Nodes: 29.0 5742 10
Got to depth: 2 Searched Nodes: 30.0 Evaluated Nodes: 83.0 5742 31
Got to depth: 3 Searched Nodes: 228.0 Evaluated Nodes: 5281.0 5742 343
Got to depth: 4 Searched Nodes: 3582.0 Evaluated Nodes: 12278.0 4528 949
Got to depth: 5 Searched Nodes: 24305.0 Evaluated Nodes: 420681.0 5742 5691
Got to depth: 6 Searched Nodes: 527718.0 Evaluated Nodes: 1883685.0 4528 31809
```
