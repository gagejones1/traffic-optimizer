import { useEffect, useState } from "react";
import type { ChangeEvent, FormEvent } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import "./App.css";

type Analysis = {
  id: number;
  intersectionName: string;
  northbound: number;
  southbound: number;
  eastbound: number;
  westbound: number;
  currentCycleLength: number;
  recommendedCycleLength: number;
  currentNorthSouthGreen: number;
  currentEastWestGreen: number;
  recommendedNorthSouthGreen: number;
  recommendedEastWestGreen: number;
  currentEstimatedWait: number;
  optimizedEstimatedWait: number;
  improvementPercent: number;
  createdAt: string;
};

function App() {
  const [form, setForm] = useState({
    intersectionName: "",
    northbound: 0,
    southbound: 0,
    eastbound: 0,
    westbound: 0,
    currentNorthSouthGreen: 30,
    currentEastWestGreen: 30,
  });

  const [result, setResult] = useState<Analysis | null>(null);
  const [analyses, setAnalyses] = useState<Analysis[]>([]);
  const [csvFile, setCsvFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [uploadMessage, setUploadMessage] = useState("");

  const loadAnalyses = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/traffic/analyses"
      );

      const data = await response.json();
      setAnalyses(data);
    } catch (error) {
      console.error("Error loading analyses:", error);
    }
  };

  useEffect(() => {
    loadAnalyses();
  }, []);

  const handleChange = (
    event: ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = event.target;

    setForm({
      ...form,
      [name]:
        name === "intersectionName"
          ? value
          : Number(value),
    });
  };

  const analyzeTraffic = async (
    event: FormEvent
  ) => {
    event.preventDefault();
    setLoading(true);

    try {
      const response = await fetch(
        "http://localhost:8080/api/traffic/analyze",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(form),
        }
      );

      const data = await response.json();

      setResult(data);
      await loadAnalyses();
    } catch (error) {
      console.error("Error analyzing traffic:", error);
    } finally {
      setLoading(false);
    }
  };

  const uploadCsv = async () => {
    if (!csvFile) {
      setUploadMessage("Choose a CSV file first.");
      return;
    }

    const formData = new FormData();
    formData.append("file", csvFile);

    try {
      setUploadMessage("Uploading...");

      const response = await fetch(
        "http://localhost:8080/api/traffic/upload",
        {
          method: "POST",
          body: formData,
        }
      );

      if (!response.ok) {
        throw new Error("Upload failed");
      }

      const data: Analysis[] = await response.json();

      setUploadMessage(
        `${data.length} intersections analyzed successfully.`
      );

      if (data.length > 0) {
        setResult(data[0]);
      }

      await loadAnalyses();
    } catch (error) {
      console.error(error);
      setUploadMessage("CSV upload failed.");
    }
  };

  const signalData = result
    ? [
        {
          direction: "N/S Green",
          Current: result.currentNorthSouthGreen,
          Recommended: result.recommendedNorthSouthGreen,
        },
        {
          direction: "E/W Green",
          Current: result.currentEastWestGreen,
          Recommended: result.recommendedEastWestGreen,
        },
        {
          direction: "Cycle",
          Current: result.currentCycleLength,
          Recommended: result.recommendedCycleLength,
        },
      ]
    : [];

  const delayData = result
    ? [
        {
          name: "Current",
          Delay: result.currentEstimatedWait,
        },
        {
          name: "Optimized",
          Delay: result.optimizedEstimatedWait,
        },
      ]
    : [];

  return (
    <div className="app">
      <header>
        <h1>Traffic Signal Optimizer</h1>
        <p>
          Analyze intersection traffic and generate
          recommended signal timing.
        </p>
      </header>

      <main>
        <section className="panel">
          <h2>Analyze Intersection</h2>

          <form onSubmit={analyzeTraffic}>
            <label>
              Intersection Name
              <input
                type="text"
                name="intersectionName"
                value={form.intersectionName}
                onChange={handleChange}
                placeholder="Main St and 1st Ave"
                required
              />
            </label>

            <div className="grid">
              <label>
                Northbound Vehicles / Hour
                <input
                  type="number"
                  name="northbound"
                  value={form.northbound}
                  onChange={handleChange}
                  min="0"
                />
              </label>

              <label>
                Southbound Vehicles / Hour
                <input
                  type="number"
                  name="southbound"
                  value={form.southbound}
                  onChange={handleChange}
                  min="0"
                />
              </label>

              <label>
                Eastbound Vehicles / Hour
                <input
                  type="number"
                  name="eastbound"
                  value={form.eastbound}
                  onChange={handleChange}
                  min="0"
                />
              </label>

              <label>
                Westbound Vehicles / Hour
                <input
                  type="number"
                  name="westbound"
                  value={form.westbound}
                  onChange={handleChange}
                  min="0"
                />
              </label>

              <label>
                Current N/S Green
                <input
                  type="number"
                  name="currentNorthSouthGreen"
                  value={form.currentNorthSouthGreen}
                  onChange={handleChange}
                  min="15"
                />
              </label>

              <label>
                Current E/W Green
                <input
                  type="number"
                  name="currentEastWestGreen"
                  value={form.currentEastWestGreen}
                  onChange={handleChange}
                  min="15"
                />
              </label>
            </div>

            <button type="submit" disabled={loading}>
              {loading
                ? "Analyzing..."
                : "Analyze Intersection"}
            </button>
          </form>
        </section>

        <section className="panel">
          <h2>Upload Traffic CSV</h2>

          <div className="upload-row">
            <input
              type="file"
              accept=".csv"
              onChange={(event) =>
                setCsvFile(
                  event.target.files?.[0] ?? null
                )
              }
            />

            <button
              type="button"
              onClick={uploadCsv}
            >
              Upload CSV
            </button>
          </div>

          {uploadMessage && (
            <p className="upload-message">
              {uploadMessage}
            </p>
          )}
        </section>

        {result && (
          <>
            <section className="panel">
              <h2>{result.intersectionName}</h2>

              <div className="results">
                <div className="result-card">
                  <h3>Current Timing</h3>
                  <p>
                    Cycle:{" "}
                    <strong>
                      {result.currentCycleLength}s
                    </strong>
                  </p>
                  <p>
                    N/S Green:{" "}
                    <strong>
                      {result.currentNorthSouthGreen}s
                    </strong>
                  </p>
                  <p>
                    E/W Green:{" "}
                    <strong>
                      {result.currentEastWestGreen}s
                    </strong>
                  </p>
                </div>

                <div className="result-card">
                  <h3>Recommended Timing</h3>
                  <p>
                    Cycle:{" "}
                    <strong>
                      {result.recommendedCycleLength}s
                    </strong>
                  </p>
                  <p>
                    N/S Green:{" "}
                    <strong>
                      {result.recommendedNorthSouthGreen}s
                    </strong>
                  </p>
                  <p>
                    E/W Green:{" "}
                    <strong>
                      {result.recommendedEastWestGreen}s
                    </strong>
                  </p>
                </div>

                <div className="result-card">
                  <h3>Estimated Performance</h3>
                  <p>
                    Current Delay:{" "}
                    <strong>
                      {result.currentEstimatedWait}s
                    </strong>
                  </p>
                  <p>
                    Optimized Delay:{" "}
                    <strong>
                      {result.optimizedEstimatedWait}s
                    </strong>
                  </p>
                  <p>
                    Improvement:{" "}
                    <strong>
                      {result.improvementPercent}%
                    </strong>
                  </p>
                </div>
              </div>
            </section>

            <section className="panel">
              <h2>Visual Comparison</h2>

              <div className="charts">
                <div className="chart-card">
                  <h3>Signal Timing</h3>

                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={signalData}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="direction" />
                      <YAxis />
                      <Tooltip />
                      <Legend />
                      <Bar dataKey="Current" fill="#6b7280" />
                      <Bar dataKey="Recommended" fill="#2563eb" />
                    </BarChart>
                  </ResponsiveContainer>
                </div>

                <div className="chart-card">
                  <h3>Estimated Delay</h3>

                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={delayData}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="name" />
                      <YAxis />
                      <Tooltip />
                      <Bar dataKey="Delay" fill="#2563eb" />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </section>
          </>
        )}

        <section className="panel">
          <h2>Previous Analyses</h2>

          {analyses.length === 0 ? (
            <p>No analyses saved yet.</p>
          ) : (
            <div className="history">
              {analyses
                .slice()
                .reverse()
                .map((analysis) => (
                  <div
                    className="history-card"
                    key={analysis.id}
                    onClick={() =>
                      setResult(analysis)
                    }
                  >
                    <div>
                      <h3>
                        {analysis.intersectionName}
                      </h3>

                      <p>
                        {analysis.northbound +
                          analysis.southbound +
                          analysis.eastbound +
                          analysis.westbound}{" "}
                        vehicles/hour
                      </p>
                    </div>

                    <div className="history-stats">
                      <span>
                        {analysis.recommendedCycleLength}s
                        cycle
                      </span>

                      <strong>
                        {analysis.improvementPercent}%
                      </strong>
                    </div>
                  </div>
                ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;