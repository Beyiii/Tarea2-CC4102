import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression

# Función para leer el archivo y extraer los datos
def read_data(filename):
    data = pd.read_csv(filename, sep=", ", header=None, names=["v", "e", "time"], engine='python')
    data['v'] = data['v'].apply(lambda x: int(x.split('=')[1]))
    data['e'] = data['e'].apply(lambda x: int(x.split('=')[1]))
    data['time'] = data['time'].apply(lambda x: float(x.split('=')[1]))
    return data

# Función para generar el gráfico
def plot_data_with_fitting(data):
    unique_vs = data['v'].unique()
    plt.figure(figsize=(12, 8))

    for v in unique_vs:
        subset = data[data['v'] == v]
        plt.scatter(subset['e'], subset['time'], label=f'v={v}', alpha=0.6)

        # Realizar la regresión lineal
        X = subset['e'].values.reshape(-1, 1)
        y = subset['time'].values
        reg = LinearRegression().fit(X, y)
        y_pred = reg.predict(X)

        # Dibujar la línea de ajuste
        plt.plot(subset['e'], y_pred, linestyle='--', label=f'Fit v={v} (slope={reg.coef_[0]:.2f})')

    plt.xlabel('e (number of edges)')
    plt.ylabel('Time (ms)')
    plt.title('Graph Performance: Time vs Edges for Different Numbers of Vertices')
    plt.legend()
    plt.grid(True)
    plt.show()

# Main function
def main():
    filename = 'experimentos.txt'  # Cambia esto a la ruta de tu archivo
    data = read_data(filename)
    plot_data_with_fitting(data)

if __name__ == "__main__":
    main()