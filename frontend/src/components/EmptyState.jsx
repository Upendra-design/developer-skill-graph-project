export default function EmptyState({ title = 'Nothing here yet', message }) {
  return (
    <div className="state-panel empty-panel">
      <h3>{title}</h3>
      {message && <p>{message}</p>}
    </div>
  )
}
