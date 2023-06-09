import pandas as pd
from matplotlib import pyplot as plt

df = pd.read_excel('resources/data.xlsx')
times = list(df.loc[:, 'Time'])
count = list(df.loc[:, 'Avg Count'])
errors = list(df.loc[:, 'Stdev'])
plt.scatter(times, count, s=0.1)
plt.xlabel("Cantidad de particulas salientes")
plt.ylabel("Tiempo de salida (s)")
plt.show()