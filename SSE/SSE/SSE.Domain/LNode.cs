using System;

namespace SSE.Domain
{
    public class LNode
    {
        public LNode(Guid guid)
        {
            Guid = guid;
        }

        public Guid Guid { get; set; }

        public string EncKey { get; set; }

        public Guid Identifier { get; set; }
    }
}
