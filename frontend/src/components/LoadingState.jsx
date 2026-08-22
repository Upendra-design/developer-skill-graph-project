export default function LoadingState({ label = 'Loading…' }) {
  return (
    <div className="state-panel loading-panel">
      <div className="spinner" />
      <p>{label}</p>
    </div>
  )
}
