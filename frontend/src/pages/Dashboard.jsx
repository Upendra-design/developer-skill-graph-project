import { useEffect, useState } from 'react'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'
import StatCard from '../components/StatCard.jsx'

export default function Dashboard() {
  const [stats, setStats] = useState(null)
  const [status, setStatus] = useState('loading')

  const load = () => {
    setStatus('loading')
    api.getDashboard()
      .then((res) => {
        setStats(res.data)
        setStatus('ready')
      })
      .catch(() => setStatus('error'))
  }

  useEffect(load, [])

  if (status === 'loading') return <LoadingState label="Loading dashboard…" />
  if (status === 'error') return <ErrorState message="Could not load dashboard stats from CognoDB." onRetry={load} />

  return (
    <div className="page">
      <div className="page-header">
        <h1>Developer Skill &amp; Project Network</h1>
        <p className="page-subtitle">
          A graph-powered platform for exploring relationships between developers, skills, projects, and technologies.
        </p>
      </div>

      <div className="stat-grid">
        <StatCard label="Developers" value={stats.developerCount} accent="blue" />
        <StatCard label="Skills" value={stats.skillCount} accent="green" />
        <StatCard label="Projects" value={stats.projectCount} accent="purple" />
        <StatCard label="Technologies" value={stats.technologyCount} accent="orange" />
      </div>

      <div className="card info-card">
        <h3>graph database</h3>
        <p>
         A graph database is well suited for this application because developers, skills, projects, and technologies are highly interconnected.
          It makes these relationships easier to model, query, and explore through graph traversals. Explore the Developers, Projects, and Graph Explorer pages to see these relationships in action.
        </p>
      </div>
    </div>
  )
}
