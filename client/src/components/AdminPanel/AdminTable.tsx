import { useEffect, useState } from "react";
import axios from "axios";

interface Props {
  endpoint: string;
}

export default function AdminTable({ endpoint }: Props) {
  const [data, setData] = useState<any[]>([]);

  useEffect(() => {
    axios.get(`${import.meta.env.VITE_API_BASE_URL}/${endpoint}`)
      .then(res => setData(res.data))
      .catch(err => console.error(err));
  }, [endpoint]);

  return (
    <table border={1} cellPadding={10} width="100%">
      <thead>
        <tr>
          {data[0] && Object.keys(data[0]).map(key => (
            <th key={key}>{key}</th>
          ))}
          <th>Actions</th>
        </tr>
      </thead>

      <tbody>
        {data.map((row, i) => (
          <tr key={i}>
            {Object.values(row).map((v: any, idx) => (
              <td key={idx}>{String(v)}</td>
            ))}
            <td>
              <button>Edit</button>
              <button style={{ marginLeft: 10 }}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
