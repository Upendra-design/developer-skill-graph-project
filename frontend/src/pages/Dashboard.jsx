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
          A graph-backed view of how developers, skills, projects and technologies connect.
        </p>
      </div>

      <div className="stat-grid">
        <StatCard label="Developers" value={stats.developerCount} accent="blue" />
        <StatCard label="Skills" value={stats.skillCount} accent="green" />
        <StatCard label="Projects" value={stats.projectCount} accent="purple" />
        <StatCard label="Technologies" value={stats.technologyCount} accent="orange" />
      </div>

      <div className="card info-card">
        <h3>Why a graph database?</h3>
        <p>
          Questions like "which technologies has this developer been exposed to across
          every project they've worked on" or "which developers already know the tech a
          new project needs" are natural graph traversals — walk the relationships — but
          require multiple JOINs and often recursive queries in a relational database.
          Explore the <strong>Developers</strong>, <strong>Projects</strong> and{' '}
          <strong>Graph Explorer</strong> pages to see these traversals in action.
        </p>
      </div>
    </div>
  )
}
