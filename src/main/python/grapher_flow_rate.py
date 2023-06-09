import numpy as np
from matplotlib import pyplot as plt
from utils import *

# Flow rate curve for different door widths

# d = 1.2 and N = 200
flow_rate_1, error1 = calculate_flow_rate("flow-rate-d1.2-N200_")

# d = 1.8 and N = 260
flow_rate_2, error2 = calculate_flow_rate("flow-rate-d1.8-N260_")


# d = 2.4 and N = 320
flow_rate_3, error3 = calculate_flow_rate("flow-rate-d2.4-N320_")

# d = 3.0 and N = 380
flow_rate_4, error4 = calculate_flow_rate("flow-rate-d3.0-N380_")

# ----------------------------------------------
# ----------------------------------------------

# Caudal vs tiempo

caudal1 = np.array([None] * 69)
caudal2 = np.array([None] * 69)
caudal3 = np.array([None] * 69)
caudal4 = np.array([None] * 69)

calculate_caudal(flow_rate_1, caudal1, 10)
calculate_caudal(flow_rate_2, caudal2, 10)
calculate_caudal(flow_rate_3, caudal3, 10)
calculate_caudal(flow_rate_4, caudal4, 10)

fig, ax = plt.subplots()

ax.errorbar(np.arange(0, 69), caudal1, label="d=1.2", fmt='o', color='tab:blue', capsize=0, linewidth=0.01)
ax.errorbar(np.arange(0, 69), caudal2, label="d=1.8", fmt='o', color='tab:orange', capsize=0, linewidth=0.01)
ax.errorbar(np.arange(0, 69), caudal3, label="d=2.4", fmt='o', color='tab:green', capsize=0, linewidth=0.01)
ax.errorbar(np.arange(0, 69), caudal4, label="d=3.0", fmt='o', color='tab:red', capsize=0, linewidth=0.01)

plt.ylabel("Q (1/m/s)")
plt.xlabel("Tiempo (s)")
plt.legend(loc='upper left')

plt.show()


# ----------------------------------------------
# ----------------------------------------------

# caudal medio en funcion del ancho de la puerta d

caudal1_20_50 = np.array([0] * 26)
caudal2_20_50 = np.array([0] * 26)
caudal3_20_50 = np.array([0] * 26)
caudal4_20_50 = np.array([0] * 26)

for i in range(30, 56):
    caudal1_20_50[i-30] = caudal1[i]
    caudal2_20_50[i-30] = caudal2[i]
    caudal3_20_50[i-30] = caudal3[i]
    caudal4_20_50[i-30] = caudal4[i]

plt.errorbar([1.2, 1.8, 2.4, 3.0], [np.mean(caudal1_20_50), np.mean(caudal2_20_50), np.mean(caudal3_20_50), np.mean(caudal4_20_50)], [np.std(caudal1_20_50), np.std(caudal2_20_50), np.std(caudal3_20_50), np.std(caudal4_20_50)], fmt='.', linewidth=0.5)

plt.ylabel("Q (1/m/s)")
plt.xlabel("d (m)")

plt.show()

x = [1.2, 1.8, 2.4, 3.0]
y = [np.mean(caudal1_20_50), np.mean(caudal2_20_50), np.mean(caudal3_20_50), np.mean(caudal4_20_50)]

slope, intercept = np.polyfit(x, y, 1)
regression_line = slope * np.array(x) + intercept

plt.errorbar(x, y, yerr=[np.std(caudal1_20_50), np.std(caudal2_20_50), np.std(caudal3_20_50), np.std(caudal4_20_50)], fmt='.', linewidth=0.5)
plt.plot(x, regression_line, color='red', linewidth=0.5, label='Regresión lineal')

plt.ylabel("Q (1/m/s)")
plt.xlabel("d (m)")
plt.legend(loc='upper left')

plt.show()