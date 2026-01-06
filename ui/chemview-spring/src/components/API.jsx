import React, { useState } from 'react';

const ChemistrySearch = () => {
    const [elementInput, setElementInput] = useState('');
    const [chemicalInput, setChemicalInput] = useState('');
    const [elementData, setElementData] = useState(null);
    const [chemicalData, setChemicalData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const searchElement = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setElementData(null);

        try {
            const response = await fetch(`http://localhost:8080/api/chemistry/element/${encodeURIComponent(elementInput)}`);
            const data = await response.json();

            if (response.ok) {
                setElementData(data);
            } else {
                setError(data.error || 'Failed to fetch element data');
            }
        } catch (err) {
            setError('Network error: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const searchChemical = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setChemicalData(null);

        try {
            const response = await fetch(`http://localhost:8080/api/chemistry/chemical/${encodeURIComponent(chemicalInput)}`);
            const data = await response.json();

            if (response.ok) {
                setChemicalData(data);
            } else {
                setError(data.error || 'Failed to fetch chemical data');
            }
        } catch (err) {
            setError('Network error: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
            <h1 style={{ color: '#2c5032' }}>ChemView</h1>

            {/* Element Search */}
            <div style={{ marginBottom: '30px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
                <h2 style={{ color: '#3498db' }}>Periodic Table Element</h2>
                <form onSubmit={searchElement}>
                    <input
                        type="text"
                        value={elementInput}
                        onChange={(e) => setElementInput(e.target.value)}
                        placeholder="Enter element name or symbol (e.g., H or Hydrogen)"
                        style={{
                            padding: '10px',
                            width: '300px',
                            marginRight: '10px',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                    <button
                        type="submit"
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#3498db',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer'
                        }}
                    >
                        Search Element
                    </button>
                </form>

                {elementData && (
                    <div style={{
                        marginTop: '20px',
                        padding: '15px',
                        backgroundColor: '#ecf0f1',
                        borderRadius: '4px'
                    }}>
                        <h3 style={{ color: '#2c3e50' }}>Element Information</h3>
                        <p><strong>Name:</strong> {elementData.name}</p>
                        <p><strong>Appearance:</strong> {elementData.appearance}</p>
                        <p><strong>Electron Configuration:</strong> {elementData.electronConfiguration}</p>
                    </div>
                )}
            </div>

            {/* Chemical Search */}
            <div style={{ marginBottom: '30px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
                <h2 style={{ color: '#e74c3c' }}>Chemical Compound (PubChem)</h2>
                <form onSubmit={searchChemical}>
                    <input
                        type="text"
                        value={chemicalInput}
                        onChange={(e) => setChemicalInput(e.target.value)}
                        placeholder="Enter chemical name (e.g., aspirin)"
                        style={{
                            padding: '10px',
                            width: '300px',
                            marginRight: '10px',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                    <button
                        type="submit"
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#e74c3c',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer'
                        }}
                    >
                        Search Chemical
                    </button>
                </form>

                {chemicalData && (
                    <div style={{
                        marginTop: '20px',
                        padding: '15px',
                        backgroundColor: '#ecf0f1',
                        borderRadius: '4px'
                    }}>
                        <h3 style={{ color: '#2c3e50' }}>Chemical Information</h3>
                        <p><strong>Molecular Formula:</strong> {chemicalData.molecularFormula}</p>
                        <p><strong>Molecular Weight:</strong> {chemicalData.molecularWeight}</p>
                        <p><strong>IUPAC Name:</strong> {chemicalData.iupacName}</p>
                    </div>
                )}
            </div>

            {loading && (
                <div style={{
                    padding: '10px',
                    backgroundColor: '#3498db',
                    color: 'white',
                    borderRadius: '4px',
                    textAlign: 'center'
                }}>
                    Loading...
                </div>
            )}

            {error && (
                <div style={{
                    padding: '10px',
                    backgroundColor: '#e74c3c',
                    color: 'white',
                    borderRadius: '4px'
                }}>
                    Error: {error}
                </div>
            )}
        </div>
    );
};

export default ChemistrySearch;