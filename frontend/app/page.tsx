"use client";

import { useEffect, useState } from "react";

export default function Home() {
  const [status, setStatus] = useState("Checking backend...");

  useEffect(() => {
    fetch("/api/health")
      .then((response) => response.json())
      .then((data) => {
        setStatus(`${data.status} - ${data.service}`);
      })
      .catch((error) => {
  console.error("Health check failed:", error);
  setStatus("Backend connection failed");
});
  }, []);

  return (
    <main className="flex min-h-screen items-center justify-center">
      <div className="text-center">
        <h1 className="text-4xl font-bold">Vanset</h1>

        <p className="mt-4 text-lg">
          Backend Status: {status}
        </p>
      </div>
    </main>
  );
}