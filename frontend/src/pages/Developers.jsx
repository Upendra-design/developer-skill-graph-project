import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'
import EmptyState from '../components/EmptyState.jsx'

export default function Developers() {
  const [developers, setDevelopers] = useState([])
  const [search, setSearch] = useState('')
  const [status, setStatus] = useState('loading')

  const load = (query = '') => {
    setStatus('loading')
    api.getDevelopers(query)
      .then((res) => {
        setDevelopers(res.data)
        setStatus('ready')
      })
      .catch(() => setStatus('error'))
  }

  useEffect(() => { load() }, [])

  const onSearchSubmit = (e) => {
    e.preventDefault()
    load(search)
  }

  return (
    <div className="page">
      <div className="page-header">
        <h1>Developers</h1>
        <p className="page-subtitle">Browse developers and see how they connect to skills, projects and technologies.</p>
      </div>

      <form className="search-bar" onSubmit={onSearchSubmit}>
        <input
          type="text"
          placeholder="Search developers by name…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button type="submit" className="btn-primary">Search</button>
      </form>

      {status === 'loading' && <LoadingState label="Loading developers…" />}
      {status === 'error' && <ErrorState message="Could not load developers from CognoDB." onRetry={() => load(search)} />}
      {status === 'ready' && developers.length === 0 && (
        <EmptyState title="No developers found" message="Try a different search term." />
      )}

      {status === 'ready' && developers.length > 0 && (
        <div className="grid-list">
          {developers.map((dev) => (
            <Link to={`/developers/${dev.id}`} key={dev.id} className="card list-card">
              <h3>{dev.name}</h3>
              <p className="muted">{dev.email}</p>
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
