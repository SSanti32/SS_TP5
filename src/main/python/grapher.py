import pandas as pd
from matplotlib import pyplot as plt

df = pd.read_excel('resources/data.xlsx')
times = list(df.loc[:, 'Time'])
count = list(df.loc[:, 'Avg Count'])
errors = list(df.loc[:, 'Stdev'])
plt.errorbar(count, times, xerr=errors, fmt='o', color='tab:blue', ecolor='lightblue', elinewidth=3, capsize=0, linestyle='solid', linewidth=0.01, markersize=2)
plt.xlabel("Cantidad de particulas salientes")
plt.ylabel("Tiempo de salida (s)")
plt.show()