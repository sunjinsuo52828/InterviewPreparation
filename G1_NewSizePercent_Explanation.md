# G1NewSizePercent Explained

## 1. What is it?
`G1NewSizePercent` is a JVM flag specific to the **G1 Garbage Collector**.
It sets the **minimum percentage of the heap** that the **Young Generation** (Eden + Survivor regions) can occupy.

- **Flag:** `-XX:G1NewSizePercent=<N>`
- **Default Value:** **5%** (of total heap size).
- **Experimental:** In some versions, you might need `-XX:+UnlockExperimentalVMOptions` to change it, though it's standard in modern JDKs.

## 2. Why do we need it? (The "Safety Floor")
G1 is an **Adaptive** collector. Its primary goal is to meet your **Pause Time Target** (`-XX:MaxGCPauseMillis`, default 200ms).

To meet this target, G1 dynamically resizes the Young Generation:
- **If pauses are too long:** G1 **shrinks** the Young Gen (so there's less to clean).
- **If pauses are short:** G1 **expands** the Young Gen (to increase throughput).

### The Problem
If your pause time target is very aggressive (e.g., 10ms), G1 might shrink the Young Gen to a tiny size.
- **Result:** You get super short pauses, BUT they happen **constantly** (e.g., every 50ms).
- **Impact:** Your application spends all its time doing GC overhead, killing throughput.

### The Solution
`G1NewSizePercent` acts as a **hard floor**.
- Even if G1 wants to shrink Young Gen further to meet the pause time, it **cannot** go below this percentage.
- This guarantees a minimum amount of application work (throughput) between GCs.

## 3. Visual Analogy
Imagine a **Water Tank** (The Heap).
- **Young Gen** is the water you use for daily showers.
- **G1** is a smart controller that adjusts the water level based on how fast you need it.

If you tell the controller "I need water INSTANTLY" (Low Pause Time), it might give you a tiny cup of water.
- You finish the cup in 1 second and need another one. You spend all day refilling cups.

**`G1NewSizePercent`** is like saying:
> "No matter how fast I need it, **never give me less than a bucket** (5%)."

## 4. Related Flags
| Flag | Default | Description |
|------|---------|-------------|
| `-XX:G1NewSizePercent` | 5% | **Minimum** Young Gen size. |
| `-XX:G1MaxNewSizePercent` | 60% | **Maximum** Young Gen size. G1 won't expand beyond this. |
| `-XX:MaxGCPauseMillis` | 200ms | The target that drives the resizing logic. |

## 5. When to tune it?
- **Don't touch it** unless you have a specific problem.
- **Increase it** if:
    - You see extremely frequent, short Young GCs.
    - Throughput is low due to GC overhead.
- **Decrease it** (Rare):
    - If you have a massive heap (e.g., 100GB) and 5% (5GB) is still taking too long to clean.
